package com.hello;

import com.hello.gamemodule.Id.IdManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @Author：lqh
 * @Date：2024/4/3 14:07
 */
@Component
public class ShutdownHandler implements ApplicationListener<ApplicationContextEvent>, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHandler.class);

    @Autowired
    private IdManager idManager;
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        //最高优先级处理， 先关闭游戏上下文 再对组件一一关闭
        if (event instanceof ContextClosedEvent) {

            idManager.saveIdValue();

            logger.info("Game shutdown gracefully.");
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
