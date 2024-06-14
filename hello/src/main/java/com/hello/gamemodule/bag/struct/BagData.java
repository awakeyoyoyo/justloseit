package com.hello.gamemodule.bag.struct;

import lombok.Data;

import java.util.Map;

/**
 * 背包数据
 * @Author：lqh
 * @Date：2024/6/4 16:44
 */
@Data
public class BagData {

    private int bagType;

    /**
     * 物品唯一id -> 物品
     */
    private Map<String, Item> itemObjId2ItemMap;

}
