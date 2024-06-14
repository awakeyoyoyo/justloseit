package com.hello.gamemodule.bag.struct;

import java.util.Map;

/**
 * @Author：lqh
 * @Date：2024/6/14 15:49
 */
public class Equip {

    /**
     * 唯一id
     */
    private long unitId;
    /**
     * 装备id
     */
    private int equipId;

    /**
     *  特殊属性
     */
    private Map<Integer,Long> specialAttr;
}
