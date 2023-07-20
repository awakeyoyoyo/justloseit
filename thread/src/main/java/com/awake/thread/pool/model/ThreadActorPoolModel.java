package com.awake.thread.pool.model;

import com.awake.thread.anno.SafeRunnable;
import com.awake.util.StringUtils;
import com.awake.util.ThreadUtils;
import io.netty.util.concurrent.FastThreadLocalThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version : 1.0
 * @ClassName: SingleThreadActorPool
 * @Description: 基于单独线程执行实现的Actor模型
 * @Auther: awake
 * @Date: 2023/3/10 11:24
 **/
public class ThreadActorPoolModel implements IThreadPoolModel {

    private static final Logger logger = LoggerFactory.getLogger(ThreadActorPoolModel.class);

    private ExecutorService[] executors;

    public int executorsSize;


    public ThreadActorPoolModel(int executorsSize) {
        this.executorsSize = executorsSize;
        executors = new ExecutorService[executorsSize];
        for (int i = 0; i < executorsSize; i++) {
            TaskThreadFactory taskThreadFactory = new TaskThreadFactory(i);
            ExecutorService executor = Executors.newSingleThreadExecutor(taskThreadFactory);
            executors[i] = executor;
        }
    }

    @Override
    public void execute(int executorHash, Runnable runnable) {
        executors[Math.abs(executorHash % executorsSize)].execute(SafeRunnable.valueOf(runnable));
    }

    public static class TaskThreadFactory implements ThreadFactory {
        private final int poolNumber;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        public TaskThreadFactory(int poolNumber) {
            this.group = ThreadUtils.currentThreadGroup();
            this.poolNumber = poolNumber;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            String threadName = StringUtils.format("ThreadActorPoolModel-p{}-t{}", poolNumber + 1, threadNumber.getAndIncrement());
            Thread thread = new FastThreadLocalThread(group, runnable, threadName);
            thread.setDaemon(false);
            thread.setPriority(Thread.NORM_PRIORITY);
            thread.setUncaughtExceptionHandler((t, e) -> logger.error(t.toString(), e));
            return thread;
        }
    }
}
