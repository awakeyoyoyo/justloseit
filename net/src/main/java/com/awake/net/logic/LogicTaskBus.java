package com.awake.net.logic;

import com.awake.thread.pool.model.ThreadActorPoolModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version : 1.0
 * @ClassName: LogicTaskBus
 * @Description: 用来处理客户的请求，做一些cpu密集型任务，尽量避免做一些阻塞操作；IO密集型任务可以放在Event线程池去做
 * @Auther: awake
 * @Date: 2023/8/8 21:01
 **/
public class LogicTaskBus {

    private static final Logger logger = LoggerFactory.getLogger(LogicTaskBus.class);

    /**
     *  线程池的大小，也可以通过provider thread配置指定
     */
    public int executorSize;

    /**
     * 使用不同的线程池，让线程池之间实现隔离，互不影响
     */
    private ThreadActorPoolModel executors;

    private LogicTaskBus instance;

    LogicTaskBus(int executorSize) {
        this.executorSize = executorSize;
        executors= new ThreadActorPoolModel(executorSize, LogicTaskBus.class.getName());
        instance=this;
    }
}
