package com.awake.storage.interpreter.data;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: StorageHeader
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 10:57
 **/
@Data
public class StorageHeader {

    /**
     * 字段名
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 列
     */
    private int index;

    public static StorageHeader valueOf(String name, String type, int index) {
        var resourceHeader = new StorageHeader();
        resourceHeader.name = name;
        resourceHeader.type = type;
        resourceHeader.index = index;
        return resourceHeader;
    }
}
