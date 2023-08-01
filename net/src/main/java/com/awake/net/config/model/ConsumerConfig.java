package com.awake.net.config.model;

import java.util.List;

/**
 * @version : 1.0
 * @ClassName: ConsumerConfig
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/1 10:38
 **/
public class ConsumerConfig {

    private List<ConsumerModule> consumers;

    public static ConsumerConfig valueOf(List<ConsumerModule> modules) {
        ConsumerConfig config = new ConsumerConfig();
        config.consumers = modules;
        return config;
    }
}
