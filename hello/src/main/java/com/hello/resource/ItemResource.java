package com.hello.resource;

import com.awake.storage.anno.Id;
import com.awake.storage.anno.Storage;

/**
 * @Author：lqh
 * @Date：2024/6/4 16:36
 */
@Storage
public class ItemResource {

    /**
     * 道具id
     */
    @Id
    private int id;
    /**
     * 背包类型
     */
    private int bagType;
    /**
     * 道具类型
     */
    private int itemType;
    /**
     * 上限
     */
    private int limit;
    /**
     * 额外参数
     */
    private String extraParams;
}
