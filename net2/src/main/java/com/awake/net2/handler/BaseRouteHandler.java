package com.awake.net2.handler;

import com.awake.net2.NetContext;
import com.awake.net2.packet.CmdPacket;
import com.awake.net2.protocol.definition.ProtocolDefinition;
import com.awake.net2.session.Session;
import com.awake.net2.util.SessionUtils;
import com.awake.util.base.StringUtils;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author：lqh
 * @Date：2024/3/15 11:06
 */
@ChannelHandler.Sharable
public class BaseRouteHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BaseRouteHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        Session session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }
        CmdPacket cmdPacket = (CmdPacket) msg;
        //解析协议包
        ProtocolDefinition protocolDefinition = NetContext.getProtocolManager().getProtocolDefinition(cmdPacket.getProtoId());
        Codec cmdPacketCodec = ProtobufProxy.create(protocolDefinition.getProtocolClass());
        Object packet = cmdPacketCodec.decode(cmdPacket.getPacketData());
        NetContext.getRouter().receive(session, packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            logger.error("session exception caught {}", SessionUtils.sessionSimpleInfo(ctx), cause);
        } finally {
            ctx.close();
        }
    }
}
