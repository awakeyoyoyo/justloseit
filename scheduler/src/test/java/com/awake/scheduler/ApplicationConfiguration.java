package com.awake.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: ApplicationConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/7 17:49
 **/
@Configuration
@ComponentScans(value = {@ComponentScan("com.awake.scheduler")})
public class ApplicationConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public SchedulerContext schedulerContext() {
        return new SchedulerContext();
    }

}
