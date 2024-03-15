package com.awake.net2.router;

import com.awake.thread.pool.model.ThreadActorPoolModel;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @Author：lqh
 * @Date：2024/3/15 17:27
 */
public class TaskBus {
    /**
     * 使用不同的线程池，让线程池之间实现隔离，互不影响
     */
    private static ThreadActorPoolModel executors;

    public static final int EXECUTOR_SIZE;

    static {
//        var localConfig = NetContext.getConfigManager().getLocalConfig();
//        var providerConfig = localConfig.getProvider();

//        EXECUTOR_SIZE = (providerConfig == null || StringUtils.isBlank(providerConfig.getThread())) ? (Runtime.getRuntime().availableProcessors() + 1) : Integer.parseInt(providerConfig.getThread());
        EXECUTOR_SIZE = Runtime.getRuntime().availableProcessors() + 1;
        executors = new ThreadActorPoolModel(EXECUTOR_SIZE);
    }

    public static void execute(int taskExecutorHash, Runnable runnable) {
        executors.execute(taskExecutorHash, runnable);
    }

    public static CompletableFuture asyncExecuteCallable(int callBackExecutorHash, int taskExecutorHash, Callable callable) {
        return executors.asyncExecuteCallable(callBackExecutorHash, taskExecutorHash, callable);
    }

    public static CompletableFuture asyncExecuteCallable(int taskExecutorHash, Callable callable) {
        return executors.asyncExecuteCallable(taskExecutorHash, callable);
    }

    public static int calTaskExecutorHash(Object argument) {
        return executors.calTaskExecutorHash(argument);
    }

    public static Executor currentThreadExecutor() {
        return executors.currentThreadExecutor();
    }
}
