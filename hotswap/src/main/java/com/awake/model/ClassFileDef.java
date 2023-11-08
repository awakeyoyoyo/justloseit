package com.awake.model;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ClassFileDef
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/8 18:03
 **/
@Data
public class ClassFileDef {

    private String path;
    private String className;
    private byte[] data;
    private long lastModifyTime;

    public ClassFileDef(String className, String path, long lastModifyTime, byte[] data) {
        this.className = className;
        this.path = path;
        this.lastModifyTime = lastModifyTime;
        this.data = data;
    }
}
