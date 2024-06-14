package com.hello.gamemodule.bag.struct;

import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/6/14 15:24
 */
@Data
public class Item {
    /**
     * 唯一id
     */
    private long unitId;
    /**
     * 道具id
     */
    private int itemId;

    /**
     * 数量
     */
    private int num;

}
