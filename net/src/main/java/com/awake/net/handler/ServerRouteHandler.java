package com.awake.net.handler;

import com.awake.NetContext;
import com.awake.event.manger.EventBus;
import com.awake.net.event.ServerSessionActiveEvent;
import com.awake.net.event.ServerSessionInactiveEvent;
import com.awake.net.session.Session;
import com.awake.net.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: ServerRouteHandler
 * @Description: 服务端RouteHandler
 * @Auther: awake
 * @Date: 2023/8/3 19:57
 **/
@ChannelHandler.Sharable
@Component
public class ServerRouteHandler extends BaseRouteHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerRouteHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Session session = SessionUtils.initChannel(ctx.channel());
        NetContext.getNetContext().getSessionManager().addServerSession(session);
        logger.info("server channel is active {}", SessionUtils.sessionInfo(ctx));
        EventBus.publicEvent(ServerSessionActiveEvent.valueOf(session));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        Session session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }
        NetContext.getNetContext().getSessionManager().removeServerSession(session);
        logger.warn("server channel is inactive {}", SessionUtils.sessionSimpleInfo(ctx));
        EventBus.publicEvent(ServerSessionInactiveEvent.valueOf(session));
    }
}
