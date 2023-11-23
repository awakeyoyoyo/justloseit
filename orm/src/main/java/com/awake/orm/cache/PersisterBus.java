package com.awake.orm.cache;

import com.awake.orm.config.OrmProperties;
import com.awake.util.base.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ForkJoinPool;

/**
 * @version : 1.0
 * @ClassName: PersisterBus
 * @Description: 持久化线程池
 * @Auther: awake
 * @Date: 2023/11/15 16:24
 **/
public class PersisterBus implements InitializingBean {

    @Autowired
    private OrmProperties ormConfig;
    /**
     * 使用不同的线程池，让线程池之间实现隔离，互不影响
     */
    private ForkJoinPool executors;

    private static PersisterBus instance;

    public int executorSize;

    public static void execute(Runnable runnable) {
        instance.executors.execute(runnable);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.executorSize = (StringUtils.isBlank(ormConfig.getThread())) ? (Runtime.getRuntime().availableProcessors() + 1) : Integer.parseInt(ormConfig.getThread());
        this.executors = new ForkJoinPool(executorSize);
        instance = this;
    }
}
