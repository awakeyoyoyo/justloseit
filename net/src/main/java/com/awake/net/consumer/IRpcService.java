package com.awake.net.consumer;

import com.awake.net.config.model.ProtocolModule;
import com.awake.net.consumer.balancer.IConsumerLoadBalancer;
import com.awake.net.router.answer.AsyncAnswer;
import com.awake.net.router.answer.SyncAnswer;
import org.springframework.lang.Nullable;

/**
 * @version : 1.0
 * @ClassName: IConsumer
 * @Description: 连接上的服务提供者-在当前服务器视角是消费者
 * @Auther: awake
 * @Date: 2023/10/11 19:33
 **/
public interface IRpcService {

    void init();

    IConsumerLoadBalancer loadBalancer(ProtocolModule protocolModule);

    /**
     * 直接发送，不需要任何返回值
     * <p>
     * 例子：参考 com.zfoo.app.zapp.chat.controller。FrinedController 的 atApplyFriendRequest方法，客户端发起申请请求，chat服务处理后，再把消息直接发给网关
     *
     * @param packet   需要发送的包
     * @param argument 计算负载均衡的参数，比如用户的id
     */
    void send(Object packet, @Nullable Object argument);

    <T> SyncAnswer<T> syncAsk(Object packet, Class<T> answerClass, @Nullable Object argument) throws Exception;

    <T> AsyncAnswer<T> asyncAsk(Object packet, Class<T> answerClass, @Nullable Object argument);
}
