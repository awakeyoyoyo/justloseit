package com.awake.game.config;

import com.awake.NetContext;
import com.awake.ProtocolContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetModuleConfig {

    @Bean
    @ConditionalOnMissingBean
    public NetContext netContext( ) {
        return new NetContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolContext protocolContext( ) {
        return new ProtocolContext();
    }
}
