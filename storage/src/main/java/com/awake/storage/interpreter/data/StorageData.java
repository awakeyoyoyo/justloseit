package com.awake.storage.interpreter.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @version : 1.0
 * @ClassName: StorageData
 * @Description: 配置文件资源
 * @Auther: awake
 * @Date: 2023/12/1 10:40
 **/
@Data
public class StorageData {

    /**
     * 文件名
     */
    private String name;
    /**
     * 配置表字段名
     */
    private List<StorageHeader> headers = new ArrayList<>();
    /**
     * 配置表数据
     */
    private List<List<String>> rows = new ArrayList<>();

    public static StorageData valueOf(String name, List<StorageHeader> headers, List<List<String>> rows) {
        var resourceData = new StorageData();
        resourceData.name = name;
        resourceData.headers = headers;
        resourceData.rows = rows;
        return resourceData;
    }
}
