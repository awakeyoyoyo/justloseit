package com.awake.net2.router;

import com.awake.event.manger.EventBus;
import com.awake.net2.NetContext;
import com.awake.net2.event.ServerExceptionEvent;
import com.awake.net2.packet.CmdPacket;
import com.awake.net2.protocol.definition.ProtocolDefinition;
import com.awake.net2.session.Session;
import com.awake.util.base.StringUtils;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author：lqh
 * @Date：2024/3/15 17:25
 */
public class Router implements IRouter {
    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    @Override
    public void send(Session session,int protoId, Object packet) {
        if (session == null) {
            logger.error("session is null and can not be sent.");
            return;
        }
        if (packet == null) {
            logger.error("packet is null and can not be sent.");
            return;
        }
        try {
            ProtocolDefinition protocolDefinition = NetContext.getProtocolManager().getProtocolDefinition(protoId);
            Class protocolClass = protocolDefinition.getProtocolClass();
            Codec cmdPacketCodec = ProtobufProxy.create(protocolClass);
            CmdPacket cmdPacket = CmdPacket.valueOf(protoId, cmdPacketCodec.encode(packet));
            var channel = session.getChannel();
            if (!channel.isActive() || !channel.isWritable()) {
                logger.warn("send msg error, protocol=[{}] isActive=[{}] isWritable=[{}]", packet.getClass().getSimpleName(), channel.isActive(), channel.isWritable());
            }
            channel.writeAndFlush(cmdPacket);
            logger.info("send msg successful, protocol class=[{}] protoId=[{}]", packet.getClass().getSimpleName(), protoId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receive(Session session, Object packet) {
        dispatchBySession(session, packet);
    }

    @Override
    public void atReceiver(Session session, Object packet) {
        try {
            // 调用PacketReceiver,进行真正的业务处理,这个submit只是根据packet找到protocolId，然后调用对应的消息处理方法
            // 这个在哪个线程处理取决于：这个上层的PacketReceiverTask被丢到了哪个线程中
            PacketBus.route(session, packet);
        } catch (Exception e) {
            EventBus.publicEvent(ServerExceptionEvent.valueOf(session, packet, e));
            logger.error(StringUtils.format("e[uid:{}][sid:{}] unknown exception", session.getUserId(), session.getSessionId(), e.getMessage()), e);
        } catch (Throwable t) {
            logger.error(StringUtils.format("e[uid:{}][sid:{}] unknown error", session.getUserId(), session.getSessionId(), t.getMessage()), t);
        }
    }

    private void dispatchBySession(Session session, Object packet) {
        long uid = session.getUserId();
        if (uid > 0) {
            TaskBus.execute((int) uid, () -> atReceiver(session, packet));
        } else {
            TaskBus.execute((int) session.getSessionId(), () -> atReceiver(session, packet));
        }
    }

    private void dispatchByTaskExecutorHash(Session session, Object packet, int taskExecutorHash) {
        TaskBus.execute(taskExecutorHash, () -> atReceiver(session, packet));
    }

}
