package com.awake.net.consumer.balancer;

import com.awake.exception.RunException;
import com.awake.net.protocol.ProtocolManager;
import com.awake.net.session.Session;
import com.awake.util.math.RandomUtils;

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
        var module = ProtocolManager.moduleByProtocol(packet.getClass());
        var sessions = getSessionsByModule(module);

        if (sessions.isEmpty()) {
            throw new RunException("RandomConsumerLoadBalancer [protocol:{}][argument:{}], no service provides the [module:{}]", packet.getClass(), argument, module);
        }

        return RandomUtils.randomEle(sessions);
    }
}
