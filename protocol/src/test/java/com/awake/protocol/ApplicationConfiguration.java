package com.awake.protocol;


import com.awake.ProtocolContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: ProtocolConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/4 16:13
 **/
//application配置
@Configuration
@ComponentScans(@ComponentScan("com.awake.protocol"))
public class ApplicationConfiguration {


    @Configuration
    public static class ProtocolConfig {
        //引入模块
        @Bean
        @ConditionalOnMissingBean
        public ProtocolContext netContext() {
            return new ProtocolContext();
        }
    }

}
