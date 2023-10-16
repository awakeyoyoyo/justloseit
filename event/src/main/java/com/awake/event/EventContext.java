package com.awake.event;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

/**
 * @version : 1.0
 * @ClassName: EventContext
 * @Description: 事件上下文  用于关闭事件系统的线程
 * @Auther: awake
 * @Date: 2023/3/10 16:42
 **/
@Data
public class EventContext implements ApplicationListener<ApplicationContextEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(EventContext.class);

    private static EventContext instance;

    private static ApplicationContext applicationContext;

    public static EventContext getEventContext() {
        return instance;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private synchronized void shutdown() {
//        try {
//            Field field = EventBus.class.getDeclaredField("executors");
//            ReflectionUtils.makeAccessible(field);
//
//            ExecutorService[] executors = (ExecutorService[]) ReflectionUtils.getField(field, null);
//            for (ExecutorService executor : executors) {
//                ThreadUtils.shutdown(executor);
//            }
//        } catch (Throwable e) {
//            logger.error("Event thread pool failed shutdown: " + ExceptionUtils.getMessage(e));
//            return;
//        }

        logger.info("Event shutdown gracefully.");
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            // 初始化上下文
            EventContext.instance = this;
            applicationContext = event.getApplicationContext();
        } else if (event instanceof ContextClosedEvent) {
            shutdown();
        }
    }

    @Override
    public int getOrder() {
        return 30;
    }
}
