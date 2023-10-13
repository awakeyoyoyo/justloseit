package com.awake.net.config.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: NetConfig
 * @Description: 模块核心配置
 * @Auther: awake
 * @Date: 2023/7/12 15:44
 **/

@Configuration
@EnableConfigurationProperties({RegistryProperties.class
        ,ProviderProperties.class
        ,ConsumerProperties.class})
@Data
public class NetConfig {

    /**
     * 网络配置
     */
    @Autowired
    private RegistryProperties registryConfig;


    /**
     * 生产者配置
     */
    private ProviderProperties provider;

    /**
     * 消费者配置
     */
    private ConsumerProperties consumer;


}
