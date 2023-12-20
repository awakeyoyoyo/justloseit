package com.awake.protocol;

import com.awake.net.protocol.ProtocolManager;
import com.awake.net.protocol.properties.ProtocolProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: ApplicationConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/9 20:52
 **/
@Configuration
public class ApplicationConfiguration {

    @Configuration
    @EnableConfigurationProperties(ProtocolProperties.class)
    public static class NetConfig {

        //引入模块
        @Bean
        @ConditionalOnMissingBean
        public ProtocolManager protocolManager(ProtocolProperties protocolProperties) {
            return new ProtocolManager(protocolProperties);
        }
    }
}
