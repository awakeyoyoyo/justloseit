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
@EnableConfigurationProperties({ZookeeperRegistryProperties.class, ServerProperties.class})
@Data
public class NetConfig {

    /**
     * zookeeper配置
     */
    @Autowired
    private ZookeeperRegistryProperties zookeeperConfig;
    /**
     * 网络配置
     */
    @Autowired
    private ServerProperties serverProperties;



}
