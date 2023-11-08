package com.awake.scheduler.manager;


import com.awake.scheduler.SchedulerContext;
import com.awake.scheduler.enhance.SchedulerDefinition;
import com.awake.util.base.CollectionUtils;
import com.awake.util.base.StringUtils;
import com.awake.util.base.ThreadUtils;
import com.awake.util.time.TimeUtils;
import io.netty.util.concurrent.FastThreadLocalThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version : 1.0
 * @ClassName: SchedulerBus
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/6 10:22
 **/
public class SchedulerBus {

    public static final String SCHEDULER = "scheduler";

    private static final Logger logger = LoggerFactory.getLogger(SchedulerBus.class);

    //後續改成優先隊列
    private static final Map<String, SchedulerDefinition> schedulerDefMap = new ConcurrentHashMap();

    /**
     * scheduler默认只有一个单线程的线程池
     */
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new SchedulerThreadFactory(1));

    /**
     * executor创建的线程id号
     */
    private static long threadId = 0;

    /**
     * 上一次trigger触发时间
     */
    private static long lastTriggerTimestamp = 0L;


    /**
     * 在scheduler中，最小的triggerTimestamp
     */
    private static long minTriggerTimestamp = 0L;

    /**
     * 启动
     */
    public static void init() {
        executor.scheduleAtFixedRate(() -> {
            try {
                triggerPerSecond();
            } catch (Exception e) {
                logger.error("scheduler triggers an error.", e);
            }
        }, 0, TimeUtils.MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
    }

    public static class SchedulerThreadFactory implements ThreadFactory {

        private final int poolNumber;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        public SchedulerThreadFactory(int poolNumber) {
            this.group = Thread.currentThread().getThreadGroup();
            this.poolNumber = poolNumber;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            var threadName = StringUtils.format("scheduler-p{}-t{}", poolNumber, threadNumber.getAndIncrement());
            var thread = new FastThreadLocalThread(group, runnable, threadName);
            thread.setDaemon(false);
            thread.setPriority(Thread.NORM_PRIORITY);
            thread.setUncaughtExceptionHandler((t, e) -> logger.error(t.toString(), e));
            threadId = thread.getId();
            return thread;
        }

    }


    /**
     * 每一秒执行一次，如果这个任务执行时间过长超过，比如10秒，执行完成后，不会再执行10次
     */
    private static void triggerPerSecond() {
        var currentTimeMillis = TimeUtils.currentTimeMillis();

        if (CollectionUtils.isEmpty(schedulerDefMap)) {
            return;
        }


        // 有人向前调整过机器时间，重新计算scheduler里的triggerTimestamp
        // var diff = timestamp - lastTriggerTimestamp;
        if (currentTimeMillis < lastTriggerTimestamp) {
            for (SchedulerDefinition schedulerDef : schedulerDefMap.values()) {
                var nextTriggerTimestamp = TimeUtils.nextTimestampByCronExpression(schedulerDef.getCronExpression(), currentTimeMillis);
                schedulerDef.setTriggerTimestamp(nextTriggerTimestamp);
            }
            refreshMinTriggerTimestamp();
        }

        // diff > 0, 没有人调整时间或者有人向后调整过机器时间，可以忽略，因为向后调整时间时间戳一定会大于triggerTimestamp，所以一定会触发
        lastTriggerTimestamp = currentTimeMillis;

        // 如果minSchedulerTriggerTimestamp大于timestamp，说明没有可执行的scheduler
        if (currentTimeMillis < minTriggerTimestamp) {
            return;
        }

        var minTimestamp = Long.MAX_VALUE;
        var timestampZonedDataTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis), TimeUtils.DEFAULT_ZONE_ID);
        for (var scheduler : schedulerDefMap.values()) {
            var triggerTimestamp = scheduler.getTriggerTimestamp();
            if (triggerTimestamp <= currentTimeMillis) {
                // 到达触发时间，则执行runnable方法
                try {
                    scheduler.getScheduler().invoke();
                } catch (Throwable t) {
                    logger.error("scheduler任务调度未知异常", t);
                }
                // 重新设置下一次的触发时间戳
                triggerTimestamp = TimeUtils.nextTimestampByCronExpression(scheduler.getCronExpression(), timestampZonedDataTime);
                scheduler.setTriggerTimestamp(triggerTimestamp);
            }
            if (triggerTimestamp < minTimestamp) {
                minTimestamp = scheduler.getTriggerTimestamp();
            }
        }
        minTriggerTimestamp = minTimestamp;
    }


    public static void refreshMinTriggerTimestamp() {
        var minTimestamp = Long.MAX_VALUE;
        for (var scheduler : schedulerDefMap.values()) {
            if (scheduler.getTriggerTimestamp() < minTimestamp) {
                minTimestamp = scheduler.getTriggerTimestamp();
            }
        }
        minTriggerTimestamp = minTimestamp;
    }

    /**
     * 不断执行的周期循环任务
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long period, TimeUnit unit) {

        return executor.scheduleAtFixedRate(ThreadUtils.safeRunnable(runnable), 0, period, unit);
    }


    /**
     * 固定延迟执行的任务
     */
    public static ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit) {

        return executor.schedule(ThreadUtils.safeRunnable(runnable), delay, unit);
    }

    /**
     * cron表达式执行的任务
     */
    public static void scheduleCron(String schedulerName, Runnable runnable, String cron) {
        if (SchedulerContext.isStop()) {
            return;
        }

        registerScheduler(schedulerName, SchedulerDefinition.valueOf(cron, runnable));
    }

    public static Executor threadExecutor(long currentThreadId) {
        return threadId == currentThreadId ? executor : null;
    }

    public static void registerScheduler(String schedulerName, SchedulerDefinition scheduler) {
        schedulerDefMap.put(schedulerName, scheduler);
        refreshMinTriggerTimestamp();
    }

    public static void removerScheduler(String schedulerName, SchedulerDefinition scheduler) {
        schedulerDefMap.remove(schedulerName, scheduler);
    }
}
