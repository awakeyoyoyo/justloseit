package com.awake.thread.pool.model;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @version : 1.0
 * @ClassName: SingleThreadActorPool
 * @Description: 基于单独任务队列实现的actor模型
 * @Auther: awake
 * @Date: 2023/3/10 11:24
 **/
public class TaskQueuePoolDispatcher implements ThreadPoolStrategy {

    @Override
    public void dispatch(int taskExecutorHash, Runnable runnable) {

    }

    @Override
    public CompletableFuture asyncDispatch(int callBackExecutorHash, int taskExecutorHash, Callable callable) {
        return null;
    }

    @Override
    public CompletableFuture asyncDispatch(int taskExecutorHash, Callable callable) {
        return null;
    }

    @Override
    public void shutdown() {

    }


    //todo



}
