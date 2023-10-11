package com.awake.net.provider;

import com.awake.net.config.model.ProtocolModule;
import com.awake.net.provider.balancer.IConsumerLoadBalancer;
import com.awake.net.router.answer.AsyncAnswer;
import com.awake.net.router.answer.SyncAnswer;

import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: Provider
 * @Description: 服务调度和负载均衡，两个关键点：摘除故障节点，负载均衡
 * 在clientSession中选择一个可用的session，最终还是调用的IRouter中的方法
 *
 * 对于某个服务提供者的封装
 *
 * @Auther: awake
 * @Date: 2023/10/11 19:33
 **/
public class Provider implements IProvider {

    private final Map<ProtocolModule, IConsumerLoadBalancer> consumerLoadBalancerMap = new HashMap<>();

    @Override
    public void init() {

    }

    @Override
    public IConsumerLoadBalancer loadBalancer(ProtocolModule protocolModule) {
        return null;
    }

    @Override
    public void send(Object packet, Object argument) {

    }

    @Override
    public <T> SyncAnswer<T> syncAsk(Object packet, Class<T> answerClass, Object argument) throws Exception {
        return null;
    }

    @Override
    public <T> AsyncAnswer<T> asyncAsk(Object packet, Class<T> answerClass, Object argument) {
        return null;
    }
}
