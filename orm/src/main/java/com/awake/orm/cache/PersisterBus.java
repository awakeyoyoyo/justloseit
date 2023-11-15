package com.awake.orm.cache;

import java.util.concurrent.ForkJoinPool;

/**
 * @version : 1.0
 * @ClassName: PersisterBus
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:24
 **/
public class PersisterBus {

    /**
     * 使用不同的线程池，让线程池之间实现隔离，互不影响
     */
    private static ForkJoinPool executors;

    public static final int EXECUTOR_SIZE;

    static {
//        var localConfig = NetContext.getConfigManager().getLocalConfig();
//        var providerConfig = localConfig.getProvider();

//        EXECUTOR_SIZE = (providerConfig == null || StringUtils.isBlank(providerConfig.getThread())) ? (Runtime.getRuntime().availableProcessors() + 1) : Integer.parseInt(providerConfig.getThread());
        EXECUTOR_SIZE = Runtime.getRuntime().availableProcessors() + 1;
        executors = new ForkJoinPool(EXECUTOR_SIZE);
    }

    public static void execute(Runnable runnable) {
        executors.execute(runnable);
    }
}
