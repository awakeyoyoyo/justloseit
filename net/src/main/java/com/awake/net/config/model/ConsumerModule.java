package com.awake.net.config.model;

import lombok.Data;

import java.util.Objects;

/**
 * @version : 1.0
 * @ClassName: ConsumerModule
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/1 10:53
 **/
@Data
public class ConsumerModule {

    /**
     *  模块id和模块名
     */
    private ProtocolModule protocolModule;

    /**
     * 负载均衡方式
     */
    private String loadBalancer;

    /**
     * 消费哪个provider
     */
    private String consumer;

    public ConsumerModule(ProtocolModule protocolModule, String loadBalancer, String consumer) {
        this.protocolModule = protocolModule;
        this.consumer = consumer;
        this.loadBalancer = loadBalancer;
    }

    public ConsumerModule(String protocolModule, String loadBalancer, String consumer) {
        this.protocolModule = new ProtocolModule((byte) 0, protocolModule);
        this.consumer = consumer;
        this.loadBalancer = loadBalancer;
    }

    public boolean matchProvider(ProviderModule providerModule) {
        return Objects.equals(protocolModule.getName(), providerModule.getProtocolModule().getName()) && Objects.equals(consumer, providerModule.getProvider());
    }
}
