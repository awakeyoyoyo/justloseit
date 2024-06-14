package com.hello.gamemodule.bag.struct;

/**
 * 抽象道具
 * @Author：lqh
 * @Date：2024/6/13 20:41
 */
public abstract class AbstractItem implements Item {

    private long unitId;

    private int itemId;

    private int num;


    @Override
    public long getUnitId() {
        return unitId;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public long getNum() {
        return num;
    }
}
