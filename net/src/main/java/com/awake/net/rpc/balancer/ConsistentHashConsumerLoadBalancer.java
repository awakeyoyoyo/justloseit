package com.awake.net.rpc.balancer;

import com.awake.net.session.Session;

/**
 * @version : 1.0
 * @ClassName: ConsistentHashConsumerLoadBalancer
 * @Description: 哈希一致性选择消费者  todo
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
