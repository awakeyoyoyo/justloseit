package com.awake.storage.autoconfigure;

import com.awake.storage.StorageContext;
import com.awake.storage.config.StorageProperties;
import com.awake.storage.manager.StorageManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: StorageAutoConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 17:34
 **/

@Configuration
@EnableConfigurationProperties({StorageProperties.class})
public class StorageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StorageManager storageManager() {
        return new StorageManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public StorageContext storageContext() {
        return new StorageContext();
    }
}
