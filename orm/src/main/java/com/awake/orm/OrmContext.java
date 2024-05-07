package com.awake.orm;

import com.awake.orm.accessor.IAccessor;
import com.awake.orm.manager.IOrmManager;
import com.awake.orm.manager.OrmManager;
import com.awake.orm.model.IEntity;
import com.awake.orm.query.IQuery;
import com.awake.orm.query.IQueryBuilder;
import com.awake.scheduler.SchedulerContext;
import com.awake.util.ReflectionUtils;
import com.awake.util.time.Stopwatch;
import com.mongodb.client.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;


/**
 * @author awakeyoyoyo
 */
public class OrmContext implements ApplicationListener<ApplicationContextEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(OrmContext.class);

    private static OrmContext instance;

    private ApplicationContext applicationContext;

    private IAccessor accessor;

    private IQuery query;

    private IOrmManager ormManager;

    private boolean stop = false;

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }

    public static OrmContext getOrmContext() {
        return instance;
    }

    public static IAccessor getAccessor() {
        return instance.accessor;
    }

    public static <E extends IEntity<?>> IQueryBuilder<E> getQuery(Class<E> entityClazz) {
        return instance.query.builder(entityClazz);
    }

    public static IOrmManager getOrmManager() {
        return instance.ormManager;
    }

    public static boolean isStop() {
        return instance.stop;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            var stopWatch = new Stopwatch();
            stopWatch.start();
            OrmContext.instance = this;
            instance.applicationContext = event.getApplicationContext();

            instance.accessor = applicationContext.getBean(IAccessor.class);
            instance.query = applicationContext.getBean(IQuery.class);
            instance.ormManager = applicationContext.getBean(IOrmManager.class);

            instance.ormManager.initBefore();
            instance.ormManager.inject();
            instance.ormManager.initAfter();
            stopWatch.tag("[Orm]");
            stopWatch.stop();
            logger.info(stopWatch.toString());
            logger.info("[Orm] started successfully");
        } else if (event instanceof ContextClosedEvent) {
            shutdownBefore();
            shutdownBetween();
            shutdownAfter();
            logger.info("Orm shutdown gracefully.");
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }

    public static synchronized void shutdownBefore() {
        SchedulerContext.shutdown();
    }

    public static synchronized void shutdownBetween() {
        instance.stop = true;
        try {
            instance.ormManager
                    .getAllEntityCaches()
                    .forEach(it -> it.persistAll());
        } catch (Exception e) {
            logger.error("关闭服务器时，持久化缓存数据异常", e);
        } finally {
            instance.stop = true;
        }
    }

    public static synchronized void shutdownAfter() {
        try {
            var field = OrmManager.class.getDeclaredField("mongoClient");
            ReflectionUtils.makeAccessible(field);
            var mongoClient = (MongoClient) ReflectionUtils.getField(field, instance.ormManager);
            mongoClient.close();
        } catch (Exception e) {
            logger.error("关闭MongoClient数据库连接失败", e);
            return;
        }
    }
}
