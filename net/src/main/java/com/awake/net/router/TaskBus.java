package com.awake.net.router;

import com.awake.thread.pool.model.ActorPoolDispatcher;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @version : 1.0
 * @ClassName: TaskBus
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/9 20:28
 **/
public class TaskBus {
    /**
     * 使用不同的线程池，让线程池之间实现隔离，互不影响
     */
    private static ActorPoolDispatcher executors;

    public static final int EXECUTOR_SIZE;

    static {
//        var localConfig = NetContext.getConfigManager().getLocalConfig();
//        var providerConfig = localConfig.getProvider();

//        EXECUTOR_SIZE = (providerConfig == null || StringUtils.isBlank(providerConfig.getThread())) ? (Runtime.getRuntime().availableProcessors() + 1) : Integer.parseInt(providerConfig.getThread());
        EXECUTOR_SIZE = Runtime.getRuntime().availableProcessors() + 1;
        executors = new ActorPoolDispatcher(EXECUTOR_SIZE);
    }

    public static void execute(int taskExecutorHash, Runnable runnable) {
        executors.dispatch(taskExecutorHash, runnable);
    }

    public static CompletableFuture asyncExecuteCallable(int callBackExecutorHash, int taskExecutorHash, Callable callable) {
        return executors.asyncDispatch(callBackExecutorHash, taskExecutorHash, callable);
    }

    public static CompletableFuture asyncExecuteCallable(int taskExecutorHash, Callable callable) {
        return executors.asyncDispatch(taskExecutorHash, callable);
    }

    public static int calTaskExecutorHash(Object argument) {
        return executors.computeDispatchKey(argument);
    }

    public static Executor currentThreadExecutor() {
        return executors.currentThreadExecutor();
    }
}
