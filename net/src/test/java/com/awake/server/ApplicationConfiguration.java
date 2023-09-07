package com.awake.server;

import com.awake.NetContext;
import com.awake.net.protocol.ProtocolManager;
import com.awake.net.protocol.properties.ProtocolProperties;
import com.awake.net.router.PacketBus;
import com.awake.net.router.Router;
import com.awake.net.session.SessionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: ApplicationConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:21
 **/
@Configuration
@ComponentScans(value = {@ComponentScan("com.awake.server")})
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

        @Bean
        @ConditionalOnMissingBean
        public NetContext netContext() {
            return new NetContext();
        }

        @Bean
        @ConditionalOnMissingBean
        public Router router() {
            return new Router();
        }
//
        @Bean
        @ConditionalOnMissingBean
        public PacketBus packetService() {
            return new PacketBus();
        }

        @Bean
        @ConditionalOnMissingBean
        public SessionManager sessionManager() {
            return new SessionManager();
        }
    }

}
