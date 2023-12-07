package com.awake.storage.manager;

import com.awake.storage.StorageContext;
import com.awake.storage.config.StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: StorageAutoTestConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:54
 **/

@Configuration
@EnableConfigurationProperties({StorageProperties.class})
@ComponentScans(value = {@ComponentScan("com.awake.storage")})
public class StorageAutoTestConfiguration {

    /**
     * manager
     */
    @Bean
    @ConditionalOnMissingBean
    public StorageManager storageManager() {
        return new StorageManager();
    }

    /**
     * context
     */
    @Bean
    @ConditionalOnMissingBean
    public StorageContext storageContext() {
        return new StorageContext();
    }
}
