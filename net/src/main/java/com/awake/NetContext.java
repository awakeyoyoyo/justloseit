package com.awake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: NetContext
 * @Description: 网络上下文
 * @Auther: awake
 * @Date: 2023/7/12 15:23
 **/
@Component
public class NetContext implements ApplicationListener<ApplicationContextEvent>, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(NetContext.class);

    private static NetContext instance;

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            /**
             * ContextRefreshedEvent可以用于在Spring容器完全初始化之后执行一些操作。例如，我们可以在Spring容器初始化完毕之后执行一些需要依赖容器中的bean对象才能完成的操作。
             * 另外，ContextRefreshedEvent还可以用于在同一容器中多个bean之间建立关联关系。
             */
            NetContext.instance = this;
            instance.applicationContext = event.getApplicationContext();
        } else if (event instanceof ContextClosedEvent) {
            shutdownBefore();
            shutdownAfter();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public synchronized void shutdownBefore() {

    }

    public synchronized void shutdownAfter() {

    }
}
