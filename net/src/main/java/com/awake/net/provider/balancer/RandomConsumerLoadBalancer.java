package com.awake.net.provider.balancer;

import com.awake.net.session.Session;

/**
 * @version : 1.0
 * @ClassName: RandomConsumerLoadBalancer
 * @Description: 随机选择消费者
 * @Auther: awake
 * @Date: 2023/10/11 19:34
 **/
public class RandomConsumerLoadBalancer extends AbstractConsumerLoadBalancer {
    public static final RandomConsumerLoadBalancer INSTANCE = new RandomConsumerLoadBalancer();

    private RandomConsumerLoadBalancer() {
    }

    public static RandomConsumerLoadBalancer getInstance() {
        return INSTANCE;
    }

    @Override
    public Session loadBalancer(Object packet, Object argument) {
        return null;
    }
}
