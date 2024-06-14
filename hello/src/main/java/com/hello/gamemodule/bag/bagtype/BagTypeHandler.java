package com.hello.gamemodule.bag.bagtype;

/**
 * @Author：lqh
 * @Date：2024/6/14 14:57
 */
public interface BagTypeHandler {

    /**
     * 增加物品
     */
    void addItem();

    /**
     * 删除物品
     * @return
     */
    boolean removeItem();

    /**
     * 检测是否满足
     * @return
     */
    boolean checkEnough();

    /**
     * 获取物品数量
     * @return
     */
    long getItemNum();

}
