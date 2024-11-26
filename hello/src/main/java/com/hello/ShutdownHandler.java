package com.hello;

import com.hello.frame.manger.IModuleManager;
import com.hello.gamemodule.Id.IdManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @Author：lqh
 * @Date：2024/4/3 14:07
 */
@Component
public class ShutdownHandler implements ApplicationListener<ApplicationContextEvent>, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHandler.class);


    private final IdManager idManager;

    public ShutdownHandler(IdManager idManager) {
        this.idManager = idManager;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        //最高优先级处理， 先关闭游戏上下文 再对组件一一关闭
        if (event instanceof ContextClosedEvent) {
            Map<String, IModuleManager> managerMap = event.getApplicationContext().getBeansOfType(IModuleManager.class);
            List<IModuleManager> iModuleManagers = new ArrayList<>(managerMap.values());
            iModuleManagers.sort(Comparator.comparingInt(IModuleManager::order));
            for (IModuleManager iModuleManager : iModuleManagers) {
                iModuleManager.shutDown();
            }
            logger.info("[Game] shutdown gracefully.");
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
