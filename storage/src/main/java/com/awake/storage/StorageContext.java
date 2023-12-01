package com.awake.storage;

import com.awake.storage.manager.IStorageManager;
import com.awake.storage.util.function.Func1;
import com.awake.util.time.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * @version : 1.0
 * @ClassName: StorageContext
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 17:31
 **/
public class StorageContext implements ApplicationListener<ApplicationContextEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(StorageContext.class);

    private static StorageContext instance;

    private ApplicationContext applicationContext;

    private IStorageManager storageManager;

    public static StorageContext getStorageContext() {
        return instance;
    }

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }

    public static IStorageManager getStorageManager() {
        return instance.storageManager;
    }

    public static <V, K> V get(Class<V> clazz, K id) {
        return instance.storageManager.getStorage(clazz).get(id);
    }

    public static <INDEX, V> List<V> getIndexes(Class<V> clazz, Func1<V, INDEX> func, INDEX index) {
        return instance.storageManager.getStorage(clazz).getIndexes(func, index);
    }

    public static <INDEX, V> V getUniqueIndex(Class<V> clazz, Func1<V, INDEX> func, INDEX uindex) {
        return instance.storageManager.getStorage(clazz).getUniqueIndex(func, uindex);
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {

        if (event instanceof ContextRefreshedEvent) {
            var stopWatch = new Stopwatch();
            stopWatch.start();
            // 初始化上下文
            StorageContext.instance = this;
            instance.applicationContext = event.getApplicationContext();
            instance.storageManager = applicationContext.getBean(IStorageManager.class);

            // 初始化，并读取配置表
            instance.storageManager.initBefore();

            // 注入配置表资源
            instance.storageManager.inject();

            // 移除没有被引用的不必要资源，为了节省服务器内存
            instance.storageManager.initAfter();
            stopWatch.tag("[Storage]");
            stopWatch.stop();
            logger.info("Storage started successfully");
            logger.info(stopWatch.toString());
        } else if (event instanceof ContextClosedEvent) {

        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
