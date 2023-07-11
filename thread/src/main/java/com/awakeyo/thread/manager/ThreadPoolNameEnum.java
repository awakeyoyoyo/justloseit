package com.awakeyo.thread.manager;

/**
 * @version : 1.0
 * @ClassName: ThreadPoolNameEnum
 * @Description: 线程池命名
 * @Auther: awake
 * @Date: 2023/4/23 17:06
 **/
public enum  ThreadPoolNameEnum {
    /**
     * 游戏线程池： 固定线程执行的线程池
     */
    GameFixThreadPool(1),
    /**
     * 入库线程池： 消费者线程池
     */
    DbFolkJoinThreadPool(2),
    /**
     * 事件线程池： 固定线程执行的线程池
     */
    EventFixThreadPool(1),
    ;

    ThreadPoolNameEnum(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }
}
