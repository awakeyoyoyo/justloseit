package com.awake.server;

import com.awake.NetContext;
import com.awake.net.config.ConfigManager;
import com.awake.net.config.model.ConsumerProperties;
import com.awake.net.config.model.NetConfig;
import com.awake.net.config.model.ProviderProperties;
import com.awake.net.config.model.RegistryProperties;
import com.awake.net.rpc.RpcService;
import com.awake.net.router.PacketManager;
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
    @EnableConfigurationProperties({RegistryProperties.class
            , ProviderProperties.class
            , ConsumerProperties.class})
    public static class NetAutoConfiguration {

        //配置-整合
        @Bean
        @ConditionalOnMissingBean
        public NetConfig netConfig() {
            return new NetConfig();
        }

        //配置管理
        @Bean
        @ConditionalOnMissingBean()
        public ConfigManager configManager() {
            return new ConfigManager();
        }

        @Bean
        @ConditionalOnMissingBean
        public RpcService rpcService() {
            return new RpcService();
        }

        //路由
        @Bean
        @ConditionalOnMissingBean
        public Router router() {
            return new Router();
        }

        //协议管理
        @Bean
        @ConditionalOnMissingBean
        public PacketManager packetBus() {
            return new PacketManager();
        }

        //会话状态管理
        @Bean
        @ConditionalOnMissingBean
        public SessionManager sessionManager() {
            return new SessionManager();
        }

        //上下文管理
        @Bean
        @ConditionalOnMissingBean
        public NetContext netContext() {
            return new NetContext();
        }
    }

}
