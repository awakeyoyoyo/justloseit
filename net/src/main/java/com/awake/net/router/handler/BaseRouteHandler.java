package com.awake.net.router.handler;

import com.awake.net.packet.DecodedPacketInfo;
import com.awake.net.session.Session;
import com.awake.net.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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

    private static final Logger logger = LoggerFactory.getLogger(BaseRouteHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Session session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }
        DecodedPacketInfo decodedPacketInfo = (DecodedPacketInfo) msg;
        //todo 分发逻辑
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
