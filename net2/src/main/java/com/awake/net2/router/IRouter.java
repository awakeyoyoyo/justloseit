package com.awake.net2.router;

import com.awake.net2.session.Session;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;

/**
 * @Author：lqh
 * @Date：2024/3/15 14:09
 */
public interface IRouter {


    /**
     * send()和receive()是消息的发送和接收的入口，可以直接调用
     * @param session
     * @param packet
     */
    void send(Session session,int protoId, GeneratedMessage packet);

    /**
     * 在服务端收到数据后，会调用这个方法. 这个方法在BaseRouteHandler.java的channelRead中被调用
     *
     * @param session
     * @param protoId
     * @param packet
     */
    void receive(Session session, int protoId, ByteString packet);

    void atReceiver(Session session, int protoId, ByteString packet);

}
