package com.awake.orm.entity.bag;

import lombok.Data;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: BagItem
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 11:42
 **/
@Data
public class ItemBag {
    private int id;
    private String desc;

    private Map<String, Item> itemMap;

    public ItemBag() {
    }

    public ItemBag(int id, String desc, Map<String, Item> itemMap) {
        this.id = id;
        this.desc = desc;
        this.itemMap = itemMap;
    }

}
