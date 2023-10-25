package com.awake;

import com.awake.net.config.IConfigManager;
import com.awake.net.consumer.IConsumer;
import com.awake.net.protocol.IProtocolManager;
import com.awake.net.router.IRouter;
import com.awake.net.router.PacketBus;
import com.awake.net.router.task.TaskBus;
import com.awake.net.server.AbstractClient;
import com.awake.net.server.AbstractServer;
import com.awake.net.session.ISessionManager;
import com.awake.thread.pool.model.ThreadActorPoolModel;
import com.awake.util.ExceptionUtils;
import com.awake.util.IOUtils;
import com.awake.util.ReflectionUtils;
import com.awake.util.time.Stopwatch;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.lang.reflect.Field;

/**
 * @version : 1.0
 * @ClassName: NetContext
 * @Description: 网络上下文
 * @Auther: awake
 * @Date: 2023/7/12 15:23
 **/
@Getter
public class NetContext implements ApplicationListener<ApplicationContextEvent>, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(NetContext.class);

    private static NetContext instance;

    private static ISessionManager sessionManager;

    private static IConfigManager configManager;

    private static IRouter router;

    private static IConsumer consumer;

    private static PacketBus packetBus;

    private static ApplicationContext applicationContext;

    private static IProtocolManager protocolManager;

    public static IProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            var stopWatch = new Stopwatch();
            stopWatch.start();
            /**
             * ContextRefreshedEvent可以用于在Spring容器完全初始化之后执行一些操作。例如，我们可以在Spring容器初始化完毕之后执行一些需要依赖容器中的bean对象才能完成的操作。
             * 另外，ContextRefreshedEvent还可以用于在同一容器中多个bean之间建立关联关系。
             */
            NetContext.instance = this;
            applicationContext = event.getApplicationContext();
            configManager = applicationContext.getBean(IConfigManager.class);
            packetBus = applicationContext.getBean(PacketBus.class);
            router = applicationContext.getBean(IRouter.class);
            sessionManager = applicationContext.getBean(ISessionManager.class);
            protocolManager = applicationContext.getBean(IProtocolManager.class);
            //初始化packet
            packetBus.init(event.getApplicationContext());
            configManager.initRegistry();
            consumer.init();
            stopWatch.tag("[Net]");
            stopWatch.stop();
            logger.info("Net started successfully");
            logger.info(stopWatch.toString());
        } else if (event instanceof ContextClosedEvent) {
            shutdownBefore();
            shutdownAfter();
        }
    }

    public static NetContext getNetContext() {
        return instance;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public synchronized void shutdownBefore() {

    }

    public synchronized void shutdownAfter() {
        // 关闭zookeeper的客户端
//        configManager.getRegistry().shutdown();

        // 先关闭所有session
        sessionManager.forEachClientSession(it -> IOUtils.closeIO(it));
        sessionManager.forEachServerSession(it -> IOUtils.closeIO(it));

        // 关闭客户端和服务器
        AbstractClient.shutdown();
        AbstractServer.shutdownAllServers();

        // 关闭TaskBus
        try {
            Field field = TaskBus.class.getDeclaredField("executors");
            ReflectionUtils.makeAccessible(field);

            var poolModel = (ThreadActorPoolModel) ReflectionUtils.getField(field, null);
            poolModel.shutdown();
        } catch (Throwable e) {
            logger.error("Net thread pool failed shutdown: " + ExceptionUtils.getMessage(e));
            return;
        }
        logger.info("Net shutdown gracefully.");
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static IRouter getRouter() {
        return router;
    }


    public static IConfigManager getConfigManager() {
        return configManager;
    }

    public static ISessionManager getSessionManager() {
        return sessionManager;
    }

    public static IConsumer getConsumer() {
        return consumer;
    }

}
