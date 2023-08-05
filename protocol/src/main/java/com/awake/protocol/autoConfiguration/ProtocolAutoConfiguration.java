package com.awake.protocol.autoConfiguration;

import com.awake.ProtocolContext;
import com.awake.protocol.ProtocolManager;
import com.awake.protocol.properties.ProtocolProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: ProtocolConfig
 * @Description: protocol自动配置类
 * @Auther: awake
 * @Date: 2023/8/4 15:52
 **/

@Configuration
@EnableConfigurationProperties(ProtocolProperties.class)
@ConditionalOnBean(ProtocolContext.class)
public class ProtocolAutoConfiguration {

    @Bean
    public ProtocolManager protocolManager(ProtocolProperties protocolProperties) {
        return new ProtocolManager(protocolProperties);
    }

}
