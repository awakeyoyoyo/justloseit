package com.awake.thread.pool.model;

import com.awake.thread.anno.SafeRunnable;

import java.util.concurrent.CompletableFuture;

/**
 * @version : 1.0
 * @ClassName: SingleThreadActorPool
 * @Description: 基于单独任务队列实现的actor模型
 * @Auther: awake
 * @Date: 2023/3/10 11:24
 **/
public class TaskQueueActorPoolModel implements IThreadPoolModel {
    //todo
    @Override
    public void execute(int taskExecutorHash, Runnable runnable) {

    }

    @Override
    public void asyncExecute(int taskExecutorHash, SafeRunnable runnable) {

    }

    @Override
    public CompletableFuture asyncExecuteCallBack(int taskExecutorHash, SafeRunnable runnable) {
        return null;
    }
}
