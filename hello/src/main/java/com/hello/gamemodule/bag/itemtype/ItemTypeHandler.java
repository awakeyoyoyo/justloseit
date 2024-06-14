package com.hello.gamemodule.bag.itemtype;

/**
 * 物品类型处理器
 * @Author：lqh
 * @Date：2024/6/14 15:19
 */
public interface ItemTypeHandler {

    /**
     * 使用效果
     */
    void use();

    /**
     * 检测是否可以使用
     * @return
     */
    boolean verifyCanUse();
}
