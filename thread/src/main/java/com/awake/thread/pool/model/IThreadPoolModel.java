package com.awake.thread.pool.model;


import com.awake.thread.anno.SafeRunnable;

import java.util.concurrent.CompletableFuture;

/**
 * @version : 1.0
 * @ClassName: ThreadPool
 * @Description: 线程池接口
 * @Auther: awake
 * @Date: 2023/3/7 16:40
 **/
public interface IThreadPoolModel {

    /**
     * 执行任务
     *
     * @param taskExecutorHash
     * @param runnable
     */
    void execute(int taskExecutorHash, Runnable runnable);

    /**
     * 異步執行邏輯 無返回
     * @param taskExecutorHash
     * @param runnable
     */
    void asyncExecute(int taskExecutorHash, SafeRunnable runnable);

    /**
     * 異步執行返回
     * @param taskExecutorHash
     * @param runnable
     * @return
     */
    CompletableFuture asyncExecuteCallBack(int taskExecutorHash, SafeRunnable runnable);
}
