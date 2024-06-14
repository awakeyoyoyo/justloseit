package com.hello.gamemodule.bag.bagtype;

/**
 * @Author：lqh
 * @Date：2024/6/14 14:57
 */
public interface BagTypeHandler {

    void addItem();

    boolean removeItem();

    boolean checkEnough();

    long getItemNum();

}
