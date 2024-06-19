package com.hello;

import com.hello.gamemodule.Id.IdManager;
import com.hello.config.GameServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
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

    private static GameContext ins;


    private final GameServerProperties gameServerProperties;
    private final ApplicationContext applicationContext;
    private final IdManager idManager;

    public GameContext(GameServerProperties gameServerProperties, ApplicationContext applicationContext, IdManager idManager) {
        this.gameServerProperties = gameServerProperties;
        this.applicationContext = applicationContext;
        this.idManager = idManager;
    }

    public GameServerProperties getGameServerProperties() {
        return gameServerProperties;
    }

    public IdManager getIdManager() {
        return idManager;
    }


    public static GameContext getIns() {
        return ins;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        //最低优先级处理，组件初始化完毕再初始化游戏上下文
        if (event instanceof ContextRefreshedEvent) {
            // 注意此时所有组件都已经注销完了，此时一般不做任何处理
            ins =this;
            idManager.initIdValue();
            logger.info("[Game] started successfully.");
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    /**
     * 获取注入spring 服务组件
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> T getComponet(Class<T> clazz) {
        T bean = applicationContext.getBean(clazz);
        return bean;
    }
}
