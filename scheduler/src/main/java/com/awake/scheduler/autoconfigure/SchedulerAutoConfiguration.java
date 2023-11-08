package com.awake.scheduler.autoconfigure;

import com.awake.scheduler.SchedulerContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: SchedulerAutoConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/6 10:57
 **/
@Configuration
public class SchedulerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SchedulerContext schedulerContext() {
        return new SchedulerContext();
    }

}
