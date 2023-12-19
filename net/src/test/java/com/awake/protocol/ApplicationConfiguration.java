package com.awake.protocol;


import com.awake.net.router.PacketManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    public static class NetConfig {

        //引入模块
        @Bean
        @ConditionalOnMissingBean
        public PacketManager packetManager() {
            return new PacketManager();
        }
    }
}
