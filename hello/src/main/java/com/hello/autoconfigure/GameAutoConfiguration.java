package com.hello.autoconfigure;

import com.hello.config.GameServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:35
 */
@Configuration
@EnableConfigurationProperties({GameServerProperties.class})
public class GameAutoConfiguration {
}
