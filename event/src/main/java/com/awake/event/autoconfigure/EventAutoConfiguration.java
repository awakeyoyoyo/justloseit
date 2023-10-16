package com.awake.event.autoconfigure;

import com.awake.event.EventContext;
import com.awake.event.processor.EventRegisterProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: EventAutoConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/16 10:52
 **/
@Configuration
public class EventAutoConfiguration {


    /**
     * 配置-整合
     */
    @Bean
    @ConditionalOnMissingBean
    public EventContext eventContext() {
        return new EventContext();
    }

    /**
     * 配置-整合
     */
    @Bean
    @ConditionalOnMissingBean
    public EventRegisterProcessor eventRegisterProcessor() {
        return new EventRegisterProcessor();
    }
}
