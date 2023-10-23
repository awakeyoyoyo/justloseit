package com.awake.net.consumer.balancer;

import com.awake.net.router.attachment.SignalAttachment;
import com.awake.net.session.Session;
import org.springframework.lang.Nullable;

/**
 * @version : 1.0
 * @ClassName: IConsumerLoadBalancer
 * @Auther: awake
 * @Date: 2023/10/11 18:19
 **/
public interface IConsumerLoadBalancer {

    /**
     * 只有一致性hash会使用这个argument参数，如果在一致性hash没有传入argument默认使用随机负载均衡
     *
     * @param packet   请求包
     * @param argument 计算参数
     * @return 一个服务提供者的session
     */
    Session loadBalancer(Object packet, @Nullable Object argument);

    default void beforeLoadBalancer(Session session, Object packet, SignalAttachment attachment) {
    }

    default void afterLoadBalancer(Session session, Object packet, SignalAttachment attachment) {
    }

}
