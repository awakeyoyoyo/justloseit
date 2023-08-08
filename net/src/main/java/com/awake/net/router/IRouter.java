package com.awake.net.router;

import com.awake.net.packet.IPacket;
import com.awake.net.router.answer.AsyncAnswer;
import com.awake.net.router.answer.SyncAnswer;
import com.awake.net.router.attachment.IAttachment;
import com.awake.net.session.Session;
import org.springframework.lang.Nullable;

/**
 * @version : 1.0
 * @ClassName: IRouter
 * @Description: 路由
 * @Auther: awake
 * @Date: 2023/8/8 19:47
 **/
public interface IRouter {
    /**
     * send()和receive()是消息的发送和接收的入口，可以直接调用
     * @param session
     * @param packet
     */
    void send(Session session, IPacket packet);

    void send(Session session, IPacket packet, @Nullable IAttachment attachment);

    /**
     * 在服务端收到数据后，会调用这个方法. 这个方法在BaseRouteHandler.java的channelRead中被调用
     * @param session
     * @param packet
     * @param attachment
     */
    void receive(Session session, IPacket packet, @Nullable IAttachment attachment);

    void atReceiver(Session session, IPacket packet, @Nullable IAttachment attachment);

    /**
     * attention：syncAsk和asyncAsk只能客户端调用
     * 同一个客户端可以同时发送多条同步或者异步消息。
     * 服务器对每个请求消息也只能回复一条消息，不能在处理一条不同或者异步消息的时候回复多条消息。
     *
     * @param session     一个网络通信的会话
     * @param packet      一个网络通信包，消息体
     * @param answerClass 等待返回包的class类。
     *                    如果为null，则不会检查这个class类的协议号是否和返回消息体的协议号相等；
     *                    如果不为null，会检查返回包的协议号。为null的情况主要用在网关。
     * @param <T>         请求消息需要服务器返回的类型
     * @param argument    参数，主要用来计算一致性hashId。
     *                    1.IConsumer会使用这个参数计算负载到哪个服务提供者；
     *                    2.服务提供者收到请求过后会使用这个参数来计算再哪个线程执行任务；
     *                    综上所述，这个参数会在上面两种情况使用。
     * @return 服务器返回的消息Response
     * @throws Exception 如果超时或者其它异常
     */
    <T extends IPacket> SyncAnswer<T> syncAsk(Session session, IPacket packet, @Nullable Class<T> answerClass, @Nullable Object argument) throws Exception;

    <T extends IPacket> AsyncAnswer<T> asyncAsk(Session session, IPacket packet, @Nullable Class<T> answerClass, @Nullable Object argument);
}
