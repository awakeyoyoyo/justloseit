package com.awake;

import com.awake.protocol.IProtocolManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

/**
 * @version : 1.0
 * @ClassName: ProtocolAutoConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/5 16:57
 **/
@Getter
public class ProtocolContext implements ApplicationListener<ApplicationContextEvent>, Ordered {
    @Autowired
    private IProtocolManager protocolManager;

    private static ProtocolContext instance;

    private ApplicationContext applicationContext;

    public static ProtocolContext getProtocolContext() {
        return instance;
    }

    public synchronized void shutdownBefore() {

    }

    public synchronized void shutdownAfter() {

    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            /**
             * ContextRefreshedEvent可以用于在Spring容器完全初始化之后执行一些操作。例如，我们可以在Spring容器初始化完毕之后执行一些需要依赖容器中的bean对象才能完成的操作。
             * 另外，ContextRefreshedEvent还可以用于在同一容器中多个bean之间建立关联关系。
             */
            ProtocolContext.instance = this;
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
}
