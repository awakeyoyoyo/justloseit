package com.hello.gamemodule.bag.bagtype;

import com.hello.gamemodule.bag.bagtype.baghandler.ItemBagTypeHandler;

/**
 * @Author：lqh
 * @Date：2024/6/14 14:57
 */
public enum BagTypeEnum {
    /**
     * 普通道具背包
     */
    COMMON_ITEM(1, ItemBagTypeHandler.getIns()),
    /**
     * 货币背包
     */
    MONET(2, ItemBagTypeHandler.getIns()),
    /**
     * 装备背包
     */
    EQUIP(3, ItemBagTypeHandler.getIns()),

    ;

    private int bagType;

    private BagTypeHandler bagHandler;

    BagTypeEnum(int bagType, BagTypeHandler bagHandler) {
        this.bagType = bagType;
        this.bagHandler = bagHandler;
    }
}
