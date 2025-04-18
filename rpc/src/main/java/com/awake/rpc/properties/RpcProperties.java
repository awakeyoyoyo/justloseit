package com.awake.rpc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author：lqh
 * @Date：2024/4/18 16:00
 */
@Data
@ConfigurationProperties(prefix = RpcProperties.PREFIX)
public class RpcProperties {
    public static final String PREFIX = "awake.net.rpc";

    private List<Integer> providerModuleIds;      //提供服务的端口 命名格式 moduleId,moduleId,moduleId

    private int providerPort;      //提供服务的端口 命名格式 port


    private List<Integer>  consumerModuleIds;     //消费的服务  命名格式 moduleId,moduleId,moduleId

    private String center;

    private String centerHost;

}

/**
 * 消费者：
 *
 * 配置消费的服务以及对应信息，构建channel。 后续各模块Rpc消费获取该channel手写初始化Stub即可
 *
 * 服务提供者：
 *
 * 配置提供服务端口，服务信息。构建GrpcServer将服务注册进去
 */