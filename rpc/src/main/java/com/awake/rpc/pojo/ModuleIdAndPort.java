package com.awake.rpc.pojo;

import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/4/23 10:00
 */
@Data
public class ModuleIdAndPort {

    private int moduleId;

    private int port;


    public ModuleIdAndPort(int moduleId, int port) {
        this.moduleId=moduleId;
        this.port=port;
    }
}
