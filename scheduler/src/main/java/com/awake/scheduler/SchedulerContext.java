package com.awake.scheduler;

import com.awake.scheduler.anno.Scheduler;
import com.awake.scheduler.enhance.SchedulerDefinition;
import com.awake.scheduler.manager.SchedulerBus;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.ArrayUtils;
import com.awake.util.base.StringUtils;
import com.awake.util.base.ThreadUtils;
import com.awake.util.time.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @version : 1.0
 * @ClassName: SchedulerContext
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/6 10:21
 **/
public class SchedulerContext implements ApplicationListener<ApplicationContextEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerContext.class);

    private static SchedulerContext instance;

    private static boolean stop = false;

    private ApplicationContext applicationContext;


    public static SchedulerContext getSchedulerContext() {
        return instance;
    }

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }

    public static boolean isStop() {
        return stop;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            var stopWatch = new Stopwatch();
            stopWatch.start();
            // 初始化上下文
            SchedulerContext.instance = this;
            instance.applicationContext = event.getApplicationContext();
            SchedulerBus.init();
            injectScheduler();
            stopWatch.tag("[Scheduler]");
            stopWatch.stop();
            logger.info(stopWatch.toString());
            logger.info("[Scheduler] started successfully");
        } else if (event instanceof ContextClosedEvent) {
            // 反射获取executor,关闭掉
            shutdown();
        }
    }

    public void injectScheduler() {
        var componentBeans = applicationContext.getBeansWithAnnotation(Component.class);
        for (var bean : componentBeans.values()) {
            var clazz = bean.getClass();
            var methods = ReflectionUtils.getMethodsByAnnoInPOJOClass(bean.getClass(), Scheduler.class);
            if (ArrayUtils.isEmpty(methods)) {
                continue;
            }

            if (!ReflectionUtils.isPojoClass(clazz)) {
                logger.warn("The message registration class [{}] is not a POJO class, and the parent class will not be scanned", clazz);
            }

            try {
                for (var method : methods) {
                    var schedulerMethod = method.getAnnotation(Scheduler.class);

                    var paramClazzs = method.getParameterTypes();
                    if (paramClazzs.length >= 1) {
                        throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] can not have any parameters", bean.getClass(), method.getName()));
                    }

                    var methodName = method.getName();

                    if (!Modifier.isPublic(method.getModifiers())) {
                        throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] must use 'public' as modifier!", bean.getClass().getName(), methodName));
                    }

                    if (Modifier.isStatic(method.getModifiers())) {
                        throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] can not use 'static' as modifier!", bean.getClass().getName(), methodName));
                    }

                    if (!methodName.startsWith("cron")) {
                        throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] must start with 'cron' as method name!"
                                , bean.getClass().getName(), methodName));
                    }

                    var scheduler = SchedulerDefinition.valueOf(schedulerMethod.cron(), bean, method);
                    SchedulerBus.registerScheduler(methodName, scheduler);
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    public synchronized static void shutdown() {
        if (stop) {
            return;
        }

        stop = true;

        try {
            Field field = SchedulerBus.class.getDeclaredField("executor");
            ReflectionUtils.makeAccessible(field);
            var executor = (ScheduledExecutorService) ReflectionUtils.getField(field, null);
            ThreadUtils.shutdown(executor);
        } catch (Throwable e) {
            logger.error("Scheduler thread pool failed shutdown.", e);
            return;
        }

        logger.info("Scheduler shutdown gracefully.");
    }

    @Override
    public int getOrder() {
        return 31;
    }
}
