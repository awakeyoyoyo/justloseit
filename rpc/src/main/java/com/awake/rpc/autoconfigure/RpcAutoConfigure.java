package com.awake.rpc.autoconfigure;

import com.awake.rpc.properties.RpcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：lqh
 * @Date：2024/10/18 11:27
 */
@Configuration
@EnableConfigurationProperties({RpcProperties.class})
public class RpcAutoConfigure {
}
