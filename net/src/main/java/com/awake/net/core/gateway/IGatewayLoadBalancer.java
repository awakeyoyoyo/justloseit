package com.awake.net.core.gateway;

/**
 *  *网关负载均衡使用计算一致性hash的参数，如果packet继承了这个接口，则网关的一致性hash负载均衡优先使用这个接口计算一致性hash；
 * @version : 1.0
 * @ClassName: IGatewayLoadBalancer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 18:03
 *
 **/
public interface IGatewayLoadBalancer {
    Object loadBalancerConsistentHashObject();
}
