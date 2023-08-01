package com.awake.net.config.model;

import lombok.Data;

import java.util.List;

/**
 * @version : 1.0
 * @ClassName: InstanceConfig
 * @Description: 服务实例配置
 * @Auther: awake
 * @Date: 2023/8/1 10:39
 **/
@Data
public class InstanceConfig {

    /**--------------------------------------------------------------  提供服务 ----------------------------------------------------------------------------------*/

    public static final int DEFAULT_PORT = 12400;

    private String thread;

    /**
     * 地址
     */
    private String address;

    /**
     * 提供的模块
     */
    private List<ProviderModule> providers;

    /**--------------------------------------------------------------  消费服务 ----------------------------------------------------------------------------------*/
    /**
     * 模块id和模块名
     */
    private ProtocolModule protocolModule;

    /**
     * 提供者名字
     */
    private String provider;


    public static InstanceConfig valueOf(String address, List<ProviderModule> modules) {
        InstanceConfig config = new InstanceConfig();
        config.address = address;
        config.providers = modules;
        return config;
    }

}
