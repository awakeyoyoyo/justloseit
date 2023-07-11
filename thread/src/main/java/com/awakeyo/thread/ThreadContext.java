package com.awakeyo.thread;

import com.awakeyo.thread.config.ThreadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.Ordered;

/**
 * @version : 1.0
 * @ClassName: ThreadContext
 * @Description: 线程上下文
 * @Auther: awake
 * @Date: 2023/4/23 16:05
 **/
public class ThreadContext implements ApplicationListener<ApplicationContextEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(ThreadContext.class);

    private static ThreadContext instance;

    private ApplicationContext applicationContext;

    private ThreadConfig threadConfig;

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
