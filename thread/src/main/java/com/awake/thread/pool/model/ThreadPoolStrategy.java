package com.awake.thread.pool.model;


import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @version : 1.0
 * @ClassName: ThreadPool
 * @Description: 线程池接口
 * @Auther: awake
 * @Date: 2023/3/7 16:40
 **/
public interface ThreadPoolStrategy {

    /**
     * 执行任务
     *
     * @param taskExecutorHash
     * @param runnable
     */
    void dispatch(int taskExecutorHash, Runnable runnable);

    /**
     * 異步執行返回 需要指定回调执行的线程标识
     * @param taskExecutorHash
     * @param callable
     * @return
     */
    CompletableFuture<?> asyncDispatch(int callBackExecutorHash, int taskExecutorHash, Callable<?> callable);

    /**
     * 用于-同步執行返回 无需指定回调执行的线程标识
     * @param taskExecutorHash
     * @param callable
     * @return
     */
    CompletableFuture<?> asyncDispatch(int taskExecutorHash, Callable<?> callable);

    /**
     * 关闭线程池
     */
    void shutdown();
}
