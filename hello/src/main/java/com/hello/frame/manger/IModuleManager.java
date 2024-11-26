package com.hello.frame.manger;

/**
 * 管理器接口
 * @Author：lqh
 * @Date：2024/11/26 11:17
 */
public interface IModuleManager {

    /**
     * 执行顺序  order越大 越后执行
     * @return
     */
    int order();

    /**
     * 初始化
     */
    void init();

    /**
     * 停服前 执行
     */
    void shutDown();
}
