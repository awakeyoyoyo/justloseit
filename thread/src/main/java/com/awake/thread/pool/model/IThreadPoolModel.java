package com.awake.thread.pool.model;


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

}
