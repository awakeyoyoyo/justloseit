package com.hello.gamemodule.bag.bagtype.baghandler;

import com.hello.gamemodule.bag.bagtype.BagTypeHandler;

/**
 * 通用道具背包处理器
 * @Author：lqh
 * @Date：2024/6/14 15:06
 */
public class ItemBagTypeHandler implements BagTypeHandler {
    public static ItemBagTypeHandler getIns() {
        return new ItemBagTypeHandler();
    }

    @Override
    public void addItem() {

    }

    @Override
    public boolean removeItem() {
        return false;
    }

    @Override
    public boolean checkEnough() {
        return false;
    }

    @Override
    public long getItemNum() {
        return 0;
    }

}
