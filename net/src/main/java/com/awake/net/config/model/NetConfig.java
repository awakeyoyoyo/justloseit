package com.awake.net.config.model;

import com.awake.net.rpc.registry.RegisterVO;
import lombok.Data;

import javax.annotation.Resource;

/**
 * @version : 1.0
 * @ClassName: NetConfig
 * @Description: 模块核心配置
 * @Auther: awake
 * @Date: 2023/7/12 15:44
 **/

@Data
public class NetConfig {

    /**
     * 注册中心配置
     */
    @Resource
    private RegistryProperties registryConfig;

    /**
     * 生产者配置
     */
    @Resource
    private ProviderProperties provider;

    /**
     * 消费者配置
     */
    @Resource
    private ConsumerProperties consumer;


    public RegisterVO toLocalRegisterVO() {
        return RegisterVO.valueOf(provider, consumer);
    }
}
