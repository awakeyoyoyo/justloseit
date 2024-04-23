package com.awake.net2.rpc.pojo;

import com.awake.util.net.HostAndPort;
import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/4/23 17:27
 */
@Data
public class GrpcClientDefinition {

    private int moduleId;

    private HostAndPort hostAndPort;

    public GrpcClientDefinition(int moduleId, HostAndPort hostAndPort) {
        this.moduleId = moduleId;
        this.hostAndPort = hostAndPort;
    }
}
