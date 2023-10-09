package com.awake.net.router.handler;

import com.awake.NetContext;
import com.awake.net.packet.DecodedPacketInfo;
import com.awake.net.session.Session;
import com.awake.net.util.SessionUtils;
import com.awake.util.base.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version : 1.0
 * @ClassName: BaseRouteHandler
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/3 18:24
 **/
@ChannelHandler.Sharable
public abstract class BaseRouteHandler extends ChannelInboundHandlerAdapter {

    public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");

    private static final Logger logger = LoggerFactory.getLogger(BaseRouteHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Session session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }
        DecodedPacketInfo decodedPacketInfo = (DecodedPacketInfo) msg;
        NetContext.getRouter().receive(session, decodedPacketInfo.getPacket(), decodedPacketInfo.getAttachment());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            logger.error("session exception caught {}", SessionUtils.sessionSimpleInfo(ctx), cause);
        } finally {
            ctx.close();
        }
    }

    public static Session initChannel(Channel channel) {
        var sessionAttr = channel.attr(SESSION_KEY);
        var session = new Session(channel);
        var setSuccessful = sessionAttr.compareAndSet(null, session);
        if (!setSuccessful) {
            channel.close();
            throw new RuntimeException(StringUtils.format("The properties of the session[channel:{}] cannot be set", channel));
        }
        return session;
    }

}
