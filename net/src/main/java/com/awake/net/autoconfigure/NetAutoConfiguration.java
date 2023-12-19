package com.awake.net.autoconfigure;

import com.awake.NetContext;
import com.awake.net.config.ConfigManager;
import com.awake.net.config.model.ConsumerProperties;
import com.awake.net.config.model.NetConfig;
import com.awake.net.config.model.ProviderProperties;
import com.awake.net.config.model.RegistryProperties;

import com.awake.net.router.PacketManager;
import com.awake.net.router.Router;
import com.awake.net.rpc.RpcService;
import com.awake.net.session.SessionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: NetAutoConfiguration
 * @Description: 自动装配
 * @Auther: awake
 * @Date: 2023/10/13 18:16
 **/
@Configuration
@EnableConfigurationProperties({RegistryProperties.class
        , ProviderProperties.class
        , ConsumerProperties.class})
public class NetAutoConfiguration {

    /**
     * 配置-整合
     */
    @Bean
    @ConditionalOnMissingBean
    public NetConfig netConfig() {
        return new NetConfig();
    }

    /**
     * 配置管理
     */
    @Bean
    @ConditionalOnMissingBean()
    public ConfigManager configManager() {
        return new ConfigManager();
    }


    /**
     * 路由
     */
    @Bean
    @ConditionalOnMissingBean
    public Router router() {
        return new Router();
    }


    @Bean
    @ConditionalOnMissingBean
    public RpcService rpcService() {
        return new RpcService();
    }

    /**
     * 协议管理
     */
    @Bean
    @ConditionalOnMissingBean
    public PacketManager packetManager() {
        return new PacketManager();
    }

    /**
     * 会话状态管理
     */
    @Bean
    @ConditionalOnMissingBean
    public SessionManager sessionManager() {
        return new SessionManager();
    }

    /**
     * 上下文管理
     */
    @Bean
    @ConditionalOnMissingBean
    public NetContext netContext() {
        return new NetContext();
    }
}