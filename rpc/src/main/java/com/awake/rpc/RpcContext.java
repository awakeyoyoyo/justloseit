package com.awake.rpc;

import com.awake.rpc.manager.IRpcManager;
import com.awake.util.time.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

/**
 * @Author：lqh
 * @Date：2024/10/16 10:12
 */
public class RpcContext implements ApplicationListener<ApplicationContextEvent>, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(RpcContext.class);

    private static RpcContext instance;
    private IRpcManager rpcManager;
    private ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }

    public static RpcContext getOrmContext() {
        return instance;
    }

    public IRpcManager getRpcManager() {
        return rpcManager;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            var stopWatch = new Stopwatch();
            stopWatch.start();
            RpcContext.instance = this;
            instance.applicationContext = event.getApplicationContext();

            instance.rpcManager = applicationContext.getBean(IRpcManager.class);

            instance.rpcManager.init();
            instance.rpcManager.start();
            stopWatch.tag("[Rpc]");
            stopWatch.stop();
            logger.info(stopWatch.toString());
            logger.info("[Rpc] started successfully");
        } else if (event instanceof ContextClosedEvent) {
            instance.rpcManager.shutdown();
            logger.info("[Rpc] shutdown gracefully.");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
