package com.awake.net2.autoconfigure;

import com.awake.net2.NetContext;
import com.awake.net2.router.PacketBus;
import com.awake.net2.router.Router;
import com.awake.net2.rpc.RpcManager;
import com.awake.net2.rpc.properties.RpcProperties;
import com.awake.net2.session.SessionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：lqh
 * @Date：2024/3/27 14:21
 */
@Configuration
@EnableConfigurationProperties({ RpcProperties.class})
public class NetAutoConfiguration {


    /**
     * 路由
     */
    @Bean
    @ConditionalOnMissingBean
    public Router router() {
        return new Router();
    }

    /**
     * 协议管理
     */
    @Bean
    @ConditionalOnMissingBean
    public PacketBus packetBus() {
        return new PacketBus();
    }

    /**
     * 会话状态管理
     */
    @Bean
    @ConditionalOnMissingBean
    public SessionManager sessionManager() {
        return new SessionManager();
    }


    //协议解析
    @Bean
    @ConditionalOnMissingBean
    public RpcManager rpcManager() {
        return new RpcManager();
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
