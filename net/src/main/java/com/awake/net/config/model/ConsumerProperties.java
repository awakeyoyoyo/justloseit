package com.awake.net.config.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @version : 1.0
 * @ClassName: ConsumerConfig
 * @Description: 声明自己消费那些模块
 * @Auther: awake
 * @Date: 2023/8/1 10:38
 **/
@Data
@ConfigurationProperties(prefix = ConsumerProperties.PREFIX)
public class ConsumerProperties {
    public static final String PREFIX = "awake.net.consumer";
    private List<ConsumerModule> consumers;

    public static ConsumerProperties valueOf(List<ConsumerModule> modules) {
        ConsumerProperties config = new ConsumerProperties();
        config.consumers = modules;
        return config;
    }
}
