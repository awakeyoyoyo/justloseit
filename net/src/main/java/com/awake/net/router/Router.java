package com.awake.net.router;

import com.awake.NetContext;
import com.awake.event.manger.EventBus;
import com.awake.net.core.gateway.event.AuthUidToGatewayEvent;
import com.awake.net.core.gateway.packet.AuthUidToGatewayCheck;
import com.awake.net.core.gateway.packet.AuthUidToGatewayConfirm;
import com.awake.net.event.ServerExceptionEvent;
import com.awake.net.packet.EncodedPacketInfo;
import com.awake.net.packet.IPacket;
import com.awake.net.packet.common.Error;
import com.awake.net.packet.common.Heartbeat;
import com.awake.net.router.answer.AsyncAnswer;
import com.awake.net.router.answer.SyncAnswer;
import com.awake.net.router.attachment.GatewayAttachment;
import com.awake.net.router.attachment.SignalAttachment;
import com.awake.net.router.exception.ErrorResponseException;
import com.awake.net.router.exception.NetTimeOutException;
import com.awake.net.router.exception.UnexpectedProtocolException;
import com.awake.net.router.task.TaskBus;
import com.awake.net.session.Session;
import com.awake.util.ExceptionUtils;
import com.awake.util.JsonUtils;
import com.awake.util.base.StringUtils;
import io.netty.util.concurrent.FastThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @version : 1.0
 * @ClassName: Router
 * @Description: 路由  负责消息的处理以及实现同步-异步-Rpc
 * @Auther: awake
 * @Date: 2023/8/8 19:47
 **/

public class Router implements IRouter {

    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    public static final long DEFAULT_TIMEOUT = 3000;

    /**
     * 路由的转发都会到异步线程池执行
     * 作为服务器接收方，会把receive收到的attachment存储在这个地方，只针对task线程。线程变量
     * atReceiver会设置attachment，但是在方法调用完成会取消，不需要过多关注。
     * asyncAsk会再次设置attachment，需要重点关注。
     */
    private final FastThreadLocal<Object> serverReceiverAttachmentThreadLocal = new FastThreadLocal<>();

    /**
     * 作为客户端，会把发送出去的signalAttachment存储在这个地方
     * key：signalId
     */
    private static final Map<Integer, SignalAttachment> signalAttachmentMap = new ConcurrentHashMap<>(1000);

    @Override
    public void receive(Session session, Object packet, Object attachment) {
        if (packet.getClass() == Heartbeat.class) {
            logger.info("heartbeat");
            return;
        }

        if (attachment == null) {
            // 正常发送消息的接收,把客户端的业务请求包装下到路由策略指定的线程进行业务处理
            // 注意：像客户端以asyncAsk发送请求，在服务器处理完后返回结果，在请求方也是进入这个receive方法，但是attachment不为空，会提前return掉不会走到这
            dispatchBySession(session, packet, null);
            return;
        }

        if (attachment.getClass() == SignalAttachment.class) {
            var signalAttachment = (SignalAttachment) attachment;
            if (signalAttachment.getClient() == SignalAttachment.SIGNAL_OUTSIDE_CLIENT) {
                // 服务器收到signalAttachment，不做任何处理
                dispatchBySession(session, packet, attachment);
            } else if (signalAttachment.getClient() == SignalAttachment.SIGNAL_NATIVE_ARGUMENT_CLIENT) {
                signalAttachment.setClient(SignalAttachment.SIGNAL_SERVER);
                dispatchByAttachment(session, packet, signalAttachment);
            } else if (signalAttachment.getClient() == SignalAttachment.SIGNAL_NATIVE_NO_ARGUMENT_CLIENT) {
                signalAttachment.setClient(SignalAttachment.SIGNAL_SERVER);
                dispatchBySession(session, packet, attachment);
            } else {
                // 客户端收到服务器应答，客户端发送的时候client为SIGNAL_NATIVE_CLIENT，服务器收到的时候将其设置为SIGNAL_SERVER
                var removedAttachment = signalAttachmentMap.remove(signalAttachment.getSignalId());
                if (removedAttachment == null) {
                    logger.error("client receives packet:[{}] [{}] and attachment:[{}] [{}] from server, but clientAttachmentMap has no attachment, perhaps timeout exception."
                            , packet.getClass().getSimpleName(), JsonUtils.object2String(packet), attachment.getClass(), JsonUtils.object2String(attachment));
                    return;
                }
                // 这里会让之前的CompletableFuture得到结果，从而像asyncAsk之类的回调到结果
                removedAttachment.getResponseFuture().complete(packet);
            }
            return;
        }
        if (attachment.getClass() == GatewayAttachment.class) {
            var gatewayAttachment = (GatewayAttachment) attachment;

            // 如：在网关监听到GatewaySessionInactiveEvent后，这时告诉home时，这个client参数设置的true
            // 注意：此时并没有return，这样子网关的消息才能发给home，在home进行处理LogoutRequest消息的处理
            if (gatewayAttachment.isClient()) {
                gatewayAttachment.setClient(false);
                dispatchByAttachment(session, packet, gatewayAttachment);
            } else {
                // 这里是：别的服务提供者提供授权给网关，比如：在玩家登录后，home服查到了玩家uid，然后发给Gateway服
                var gatewaySession = NetContext.getSessionManager().getServerSession(gatewayAttachment.getSid());
                if (gatewaySession == null) {
                    logger.warn("gateway receives packet:[{}] and attachment:[{}] from server" + ", but serverSessionMap has no session[id:{}], perhaps client disconnected from gateway.", JsonUtils.object2String(packet), JsonUtils.object2String(attachment), gatewayAttachment.getSid());
                    return;
                }

                // 网关授权，授权完成直接返回
                // 注意：这个 AuthUidToGatewayCheck 是在home的LoginController中处理完登录后，把消息发给网关进行授权
                if (AuthUidToGatewayCheck.class == packet.getClass()) {
                    var uid = ((AuthUidToGatewayCheck) packet).getUid();
                    if (uid <= 0) {
                        logger.error("错误的网关授权信息，uid必须大于0");
                        return;
                    }
                    gatewaySession.setUid(uid);
                    EventBus.publicEvent(AuthUidToGatewayEvent.valueOf(gatewaySession.getSid(), uid));

                    NetContext.getRouter().send(session, AuthUidToGatewayConfirm.valueOf(uid), new GatewayAttachment(gatewaySession));
                    return;
                }
                send(gatewaySession, packet, gatewayAttachment.getSignalAttachment());
            }
            return;
        }
//        if (attachment.getClass() == HttpAttachment.class) {
//            var httpAttachment = (HttpAttachment) attachment;
//            TaskBus.dispatchByTaskExecutorHash(httpAttachment.getTaskExecutorHash(), task);
//            return;
//        }
        dispatchBySession(session, packet, attachment);
    }

    @Override
    public void send(Session session, Object packet) {
        // 服务器异步返回的消息的发送会有signalAttachment，验证返回的消息是否满足
        var serverSignalAttachment = serverReceiverAttachmentThreadLocal.get();
        send(session, packet, serverSignalAttachment);
    }

    @Override
    public void send(Session session, Object packet, Object attachment) {
        if (session == null) {
            logger.error("session is null and can not be sent.");
            return;
        }
        if (packet == null) {
            logger.error("packet is null and can not be sent.");
            return;
        }

        var packetInfo = EncodedPacketInfo.valueOf(packet, attachment);

        var channel = session.getChannel();
        if (!channel.isActive() || !channel.isWritable()) {
            logger.warn("send msg error, protocol=[{}] isActive=[{}] isWritable=[{}]", packet.getClass().getSimpleName(), channel.isActive(), channel.isWritable());
        }
        channel.writeAndFlush(packetInfo);
    }

    @Override
    public <T extends IPacket> SyncAnswer<T> syncAsk(Session session, Object packet, Class<T> answerClass, Object argument) throws Exception {
        var clientSignalAttachment = new SignalAttachment();
        int taskExecutorHash = TaskBus.calTaskExecutorHash(argument);
        clientSignalAttachment.setTaskExecutorHash(taskExecutorHash);

        try {
            signalAttachmentMap.put(clientSignalAttachment.getSignalId(), clientSignalAttachment);
            // 里面调用的依然是：send方法发送消息
            send(session, packet, clientSignalAttachment);

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
            signalAttachmentMap.remove(clientSignalAttachment.getSignalId());
        }
    }

    /**
     * 注意：
     * 1.这个里面其实还是调用send发送的消息
     * 2.这个argument的参数，只用于provider处哪个线程执行，其实就是hashId，如：工会业务，则传入guildId，回调回来后，一定会在发起者线程。
     */
    @Override
    public <T extends IPacket> AsyncAnswer<T> asyncAsk(Session session, Object packet, Class<T> answerClass, Object argument) {
        var clientSignalAttachment = new SignalAttachment();
        var taskExecutorHash = TaskBus.calTaskExecutorHash(argument);

        clientSignalAttachment.setTaskExecutorHash(taskExecutorHash);

        // 服务器在同步或异步的消息处理中，又调用了同步或异步的方法，这时候threadReceiverAttachment不为空
        var serverSignalAttachment = serverReceiverAttachmentThreadLocal.get();

        try {
            var asyncAnswer = new AsyncAnswer<T>();
            asyncAnswer.setSignalAttachment(clientSignalAttachment);

            clientSignalAttachment.getResponseFuture()
                    // 因此超时的情况，返回的是null
                    .completeOnTimeout(null, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS).thenApply(answer -> {
                if (answer == null) {
                    throw new NetTimeOutException("async ask [{}] timeout exception", packet.getClass().getSimpleName());
                }

                if (answer.getClass() == Error.class) {
                    throw new ErrorResponseException((Error) answer);
                }

                if (answerClass != null && answerClass != answer.getClass()) {
                    throw new UnexpectedProtocolException("client expect protocol:[{}], but found protocol:[{}]", answerClass, answer.getClass().getName());
                }
                return answer;
            }).whenComplete((answer, throwable) -> {
                // 注意：进入这个方法的时机是：在上面的receive方法中，由于是asyncAsk的消息，attachment不为空，会调用CompletableFuture的complete方法
                try {
                    signalAttachmentMap.remove(clientSignalAttachment.getSignalId());
                    // 接收者在同步或异步的消息处理中，又调用了异步的方法，这时候threadServerAttachment不为空
                    if (serverSignalAttachment != null) {
                        serverReceiverAttachmentThreadLocal.set(serverSignalAttachment);
                    }
                    // 如果有异常的话，whenCompleteAsync的下一个thenAccept不会执行
                    if (throwable != null) {
                        var notCompleteCallback = asyncAnswer.getNotCompleteCallback();
                        if (notCompleteCallback != null) {
                            notCompleteCallback.run();
                        } else {
                            logger.error(ExceptionUtils.getMessage(throwable));
                        }
                        return;
                    }
                    // 异步返回，回调业务逻辑
                    asyncAnswer.setFuturePacket((T) answer);
                    asyncAnswer.consume();
                } catch (Throwable throwable1) {
                    logger.error("Asynchronous callback method [ask:{}][answer:{}] error", packet.getClass().getSimpleName(), answer.getClass().getSimpleName(), throwable1);
                } finally {
                    if (serverSignalAttachment != null) {
                        serverReceiverAttachmentThreadLocal.set(null);
                    }
                }

            });
            signalAttachmentMap.put(clientSignalAttachment.getSignalId(), clientSignalAttachment);
            // 等到上层调用whenComplete才会发送消息
            asyncAnswer.setAskCallback(() -> send(session, packet, clientSignalAttachment));
            return asyncAnswer;
        } catch (Exception e) {
            signalAttachmentMap.remove(clientSignalAttachment.getSignalId());
            throw e;
        }
    }

    @Override
    public void atReceiver(Session session, Object packet, Object attachment) {
        try {
            // 接收者（服务器）同步和异步消息的接收
            if (attachment != null) {
                serverReceiverAttachmentThreadLocal.set(attachment);
            }
            // 调用PacketReceiver,进行真正的业务处理,这个submit只是根据packet找到protocolId，然后调用对应的消息处理方法
            // 这个在哪个线程处理取决于：这个上层的PacketReceiverTask被丢到了哪个线程中
            PacketBus.route(session, packet, attachment);
        } catch (Exception e) {
            EventBus.publicEvent(ServerExceptionEvent.valueOf(session, packet, attachment, e));
            logger.error(StringUtils.format("e[uid:{}][sid:{}] unknown exception", session.getUid(), session.getSid(), e.getMessage()), e);
        } catch (Throwable t) {
            logger.error(StringUtils.format("e[uid:{}][sid:{}] unknown error", session.getUid(), session.getSid(), t.getMessage()), t);
        } finally {
            // 如果有服务器在处理同步或者异步消息的时候由于错误没有返回给客户端消息，则可能会残留serverAttachment，所以先移除
            if (attachment != null) {
                serverReceiverAttachmentThreadLocal.set(null);
            }
        }
    }

    private void dispatchBySession(Session session, Object packet, @Nullable Object attachment) {
        long uid = session.getUid();
        if (uid > 0) {
            TaskBus.execute((int) uid, () -> atReceiver(session, packet, attachment));
        } else {
            TaskBus.execute((int) session.getSid(), () -> atReceiver(session, packet, attachment));
        }
    }

    private void dispatchByAttachment(Session session, Object packet, Object attachment) {
        TaskBus.execute(((SignalAttachment) attachment).taskExecutorHash(), () -> atReceiver(session, packet, attachment));
    }


}
