package com.awake.net2.handler;

import com.awake.net2.NetContext;

import com.awake.net2.module.CommonModule;
import com.awake.net2.packet.Net2Msg;

import com.awake.net2.session.Session;
import com.awake.net2.util.SessionUtils;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
        Net2Msg.CmdPacket cmdPacket = (Net2Msg.CmdPacket) msg;
        if (cmdPacket.getProtoId() == CommonModule.Heartbeat) {
            logger.info("heartbeat from {}", SessionUtils.sessionSimpleInfo(session));
            return;
        }
        //解析协议包
        NetContext.getRouter().receive(session, cmdPacket.getProtoId(), cmdPacket.getPacketData());
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
