package com.awake.rpc.pojo;

import io.grpc.BindableService;
import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/4/23 17:03
 */
@Data
public class GrpcServiceDefinition {

    private int port;

    private int moduleId;

    private BindableService service;

    public GrpcServiceDefinition(int port, int moduleId, BindableService service) {
        this.port = port;
        this.moduleId = moduleId;
        this.service = service;
    }
}
