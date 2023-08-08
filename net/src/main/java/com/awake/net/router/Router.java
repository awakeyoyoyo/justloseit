package com.awake.net.router;

import com.awake.net.packet.IPacket;
import com.awake.net.packet.common.Heartbeat;
import com.awake.net.router.answer.AsyncAnswer;
import com.awake.net.router.answer.SyncAnswer;
import com.awake.net.router.attachment.IAttachment;
import com.awake.net.router.attachment.SignalAttachment;
import com.awake.net.session.Session;
import com.awake.util.JsonUtils;
import io.netty.util.concurrent.FastThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version : 1.0
 * @ClassName: Router
 * @Description: 路由  负责消息的处理以及实现同步-异步-Rpc
 * @Auther: awake
 * @Date: 2023/8/8 19:47
 **/
public class Router implements  IRouter{

    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    public static final long DEFAULT_TIMEOUT = 3000;

    /**
     * 路由的转发都会到异步线程池执行
     * 作为服务器接收方，会把receive收到的attachment存储在这个地方，只针对task线程。线程变量
     * atReceiver会设置attachment，但是在方法调用完成会取消，不需要过多关注。
     * asyncAsk会再次设置attachment，需要重点关注。
     *
     */
    private final FastThreadLocal<IAttachment> serverReceiverAttachmentThreadLocal = new FastThreadLocal<>();

    /**
     * 作为客户端，会把发送出去的signalAttachment存储在这个地方
     * key：signalId
     */
    private static final Map<Integer, SignalAttachment> signalAttachmentMap = new ConcurrentHashMap<>(1000);
    @Override
    public void receive(Session session, IPacket packet, IAttachment attachment) {
        if (packet.protocolId() == Heartbeat.PROTOCOL_ID) {
            logger.info("heartbeat");
            return;
        }
        // 发送者（客户端）同步和异步消息的接收，发送者通过signalId判断重复
        if (attachment != null) {
            switch (attachment.packetType()) {
                case SIGNAL_PACKET:
                    SignalAttachment signalAttachment = (SignalAttachment) attachment;

                    if (signalAttachment.isClient()) {
                        // 服务器收到signalAttachment，不做任何处理
                        signalAttachment.setClient(false);
                    } else {
                        // 客户端收到服务器应答，客户端发送的时候isClient为true，服务器收到的时候将其设置为false
                        SignalAttachment removedAttachment = signalAttachmentMap.remove(signalAttachment);
                        if (removedAttachment != null) {
                            // 这里会让之前的CompletableFuture得到结果，从而像asyncAsk之类的回调到结果
                            removedAttachment.getResponseFuture().complete(packet);
                        } else {
                            logger.error("client receives packet:[{}] and attachment:[{}] from server, but clientAttachmentMap has no attachment, perhaps timeout exception.", JsonUtils.object2String(packet), JsonUtils.object2String(attachment));
                        }
                        // 注意：这个return，这样子，asyncAsk的结果就返回了。
                        return;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void send(Session session, IPacket packet) {

    }

    @Override
    public void send(Session session, IPacket packet, IAttachment attachment) {

    }

    @Override
    public void atReceiver(Session session, IPacket packet, IAttachment attachment) {

    }

    @Override
    public <T extends IPacket> SyncAnswer<T> syncAsk(Session session, IPacket packet, Class<T> answerClass, Object argument) throws Exception {
        return null;
    }

    @Override
    public <T extends IPacket> AsyncAnswer<T> asyncAsk(Session session, IPacket packet, Class<T> answerClass, Object argument) {
        return null;
    }
}
