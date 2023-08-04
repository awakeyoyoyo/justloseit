package com.awake.protocol;

import com.awake.NetContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: ProtocolConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/4 16:13
 **/
@Configuration
@ComponentScan("com.awake.net")
public class ProtocolConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public NetContext netContext( ) {
        return new NetContext();
    }
}
