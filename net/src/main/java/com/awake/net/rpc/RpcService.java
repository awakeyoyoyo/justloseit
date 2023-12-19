package com.awake.net.rpc;

import com.awake.NetContext;
import com.awake.net.config.model.ProtocolModule;
import com.awake.net.packet.common.Error;
import com.awake.net.router.PacketManager;
import com.awake.net.router.SignalBridge;
import com.awake.net.router.TaskBus;
import com.awake.net.router.answer.AsyncAnswer;
import com.awake.net.router.answer.SyncAnswer;
import com.awake.net.router.attachment.NoAnswerAttachment;
import com.awake.net.router.attachment.SignalAttachment;
import com.awake.net.router.exception.ErrorResponseException;
import com.awake.net.router.exception.NetTimeOutException;
import com.awake.net.router.exception.UnexpectedProtocolException;
import com.awake.net.rpc.balancer.AbstractConsumerLoadBalancer;
import com.awake.net.rpc.balancer.IConsumerLoadBalancer;
import com.awake.util.JsonUtils;
import com.awake.util.base.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.awake.net.router.Router.DEFAULT_TIMEOUT;

/**
 * @version : 1.0
 * @ClassName: RpcService
 * @Description: 服务调度和负载均衡，两个关键点：摘除故障节点，负载均衡
 * 在clientSession中选择一个可用的session，最终还是调用的IRouter中的方法
 *
 *
 * @Auther: awake
 * @Date: 2023/10/11 19:33
 **/
public class RpcService implements IRpcService {
    private static final Logger logger = LoggerFactory.getLogger(RpcService.class);

    private final Map<Integer, IConsumerLoadBalancer> consumerLoadBalancerMap = new HashMap<>();

    @Override
    public void init() {
        var consumerConfig = NetContext.getConfigManager().getNetConfig().getConsumer();
        if (consumerConfig == null || CollectionUtils.isEmpty(consumerConfig.getConsumers())) {
            return;
        }
        var consumers = consumerConfig.getConsumers();
        for (var consumer : consumers) {
            consumerLoadBalancerMap.put(consumer.getProtocolModule().getId(), AbstractConsumerLoadBalancer.valueOf(consumer.getLoadBalancer()));
        }
    }

    @Override
    public IConsumerLoadBalancer loadBalancer(ProtocolModule protocolModule) {
        return consumerLoadBalancerMap.get(protocolModule.getId());
    }

    @Override
    public void send(Object packet, Object argument) {
        try {
            var loadBalancer = loadBalancer(PacketManager.moduleByProtocol(packet.getClass()));
            var session = loadBalancer.loadBalancer(packet, argument);
            var taskExecutorHash = TaskBus.calTaskExecutorHash(argument);
            NetContext.getRouter().send(session, packet, NoAnswerAttachment.valueOf(taskExecutorHash));
        } catch (Throwable t) {
            logger.error("consumer发送未知异常", t);
        }
    }

    @Override
    public <T> SyncAnswer<T> syncAsk(Object packet, Class<T> answerClass, Object argument) throws Exception {
        var loadBalancer = loadBalancer(PacketManager.moduleByProtocol(packet.getClass()));
        var session = loadBalancer.loadBalancer(packet, argument);
        // 下面的代码逻辑同Router的syncAsk，如果修改的话，记得一起修改
        var clientSignalAttachment = new SignalAttachment();
        int taskExecutorHash = TaskBus.calTaskExecutorHash(argument);
        clientSignalAttachment.setTaskExecutorHash(taskExecutorHash);

        try {
            SignalBridge.addSignalAttachment(clientSignalAttachment);
            // 里面调用的依然是：send方法发送消息
            NetContext.getRouter().send(session, packet, clientSignalAttachment);

            var responsePacket = clientSignalAttachment.getResponseFuture().get(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);

            if (responsePacket.getClass() == Error.class) {
                throw new ErrorResponseException((Error) responsePacket);
            }
            if (answerClass != null && answerClass != responsePacket.getClass()) {
                throw new UnexpectedProtocolException("client expect protocol:[{}], but found protocol:[{}]", answerClass, responsePacket.getClass().getName());
            }

            return new SyncAnswer<>((T) responsePacket, clientSignalAttachment);
        } catch (TimeoutException e) {
            throw new NetTimeOutException("syncAsk timeout exception, ask:[{}], attachment:[{}]", JsonUtils.object2String(packet), JsonUtils.object2String(clientSignalAttachment));
        } finally {
            SignalBridge.removeSignalAttachment(clientSignalAttachment);
        }
    }

    @Override
    public <T> AsyncAnswer<T> asyncAsk(Object packet, Class<T> answerClass, Object argument) {
        var loadBalancer = loadBalancer(PacketManager.moduleByProtocol(packet.getClass()));
        var session = loadBalancer.loadBalancer(packet, argument);
        var asyncAnswer = NetContext.getRouter().asyncAsk(session, packet, answerClass, argument);

        // load balancer之前调用
        loadBalancer.beforeLoadBalancer(session, packet, asyncAnswer.getSignalAttachment());

        // load balancer之后调用
        asyncAnswer.thenAccept(responsePacket -> loadBalancer.afterLoadBalancer(session, packet, asyncAnswer.getSignalAttachment()));
        return asyncAnswer;
    }
}
