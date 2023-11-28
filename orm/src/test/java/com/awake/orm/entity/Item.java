package com.awake.orm.entity;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: Item
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 11:42
 **/
@Data
public class Item {

    private int a;
    private String b;

    public Item() {
    }

    public Item(int a, String b) {
        this.a = a;
        this.b = b;
    }
}
