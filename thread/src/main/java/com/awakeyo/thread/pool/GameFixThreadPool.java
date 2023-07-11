package com.awakeyo.thread.pool;

/**
 * @version : 1.0
 * @ClassName: GameThreadBus
 * @Description: 游戏线程池
 * @Auther: awake
 * @Date: 2023/4/23 16:02
 **/
public class GameFixThreadPool {

//    // EN: The size of the thread pool can also be specified through the provider thread configuration
//    // CN: 线程池的大小，也可以通过provider thread配置指定
//    public static final int EXECUTORS_SIZE;
//
//
//    private static final SingleThreadActorPool executors ;
//
////    static {
////        var localConfig = NetContext.getConfigManager().getLocalConfig();
////        var providerConfig = localConfig.getProvider();
////
////        EXECUTORS_SIZE = (providerConfig == null || StringUtils.isBlank(providerConfig.getThread())) ? (Runtime.getRuntime().availableProcessors() + 1) : Integer.parseInt(providerConfig.getThread());
////
////        executors = new ExecutorService[EXECUTOR_SIZE];
////        for (int i = 0; i < executors.length; i++) {
////            var namedThreadFactory = new TaskThreadFactory(i);
////            var executor = Executors.newSingleThreadExecutor(namedThreadFactory);
////            executors[i] = executor;
////        }
////    }
}
