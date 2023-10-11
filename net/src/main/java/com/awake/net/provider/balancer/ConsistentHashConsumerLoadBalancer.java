package com.awake.net.provider.balancer;

import com.awake.net.session.Session;

/**
 * @version : 1.0
 * @ClassName: ConsistentHashConsumerLoadBalancer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 19:34
 **/
public class ConsistentHashConsumerLoadBalancer extends AbstractConsumerLoadBalancer {

    public static final ConsistentHashConsumerLoadBalancer INSTANCE = new ConsistentHashConsumerLoadBalancer();

    private ConsistentHashConsumerLoadBalancer() {
    }

    public static ConsistentHashConsumerLoadBalancer getInstance() {
        return INSTANCE;
    }

    @Override
    public Session loadBalancer(Object packet, Object argument) {
        return null;
    }
}
