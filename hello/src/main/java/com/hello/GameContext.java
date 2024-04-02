package com.hello;

import com.hello.gamemodule.Id.IdManager;
import com.hello.config.GameServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:39
 */
@Component
public class GameContext implements ApplicationListener<ApplicationContextEvent>,Ordered{
    private static final Logger logger = LoggerFactory.getLogger(GameContext.class);

    private static GameContext instance;

    @Autowired
    private GameServerProperties gameServerProperties;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IdManager idManager;

    public GameServerProperties getGameServerProperties() {
        return gameServerProperties;
    }

    public IdManager getIdManager() {
        return idManager;
    }


    public static GameContext getInstance() {
        return instance;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            instance=this;
            idManager.initIdValue();
        } else if (event instanceof ContextClosedEvent) {
            //...
            idManager.saveIdValue();
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
