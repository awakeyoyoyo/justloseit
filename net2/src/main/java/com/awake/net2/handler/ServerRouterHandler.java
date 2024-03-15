package com.awake.net2.handler;

import com.awake.event.manger.EventBus;
import com.awake.net2.NetContext;
import com.awake.net2.event.ServerSessionActiveEvent;
import com.awake.net2.event.ServerSessionInactiveEvent;
import com.awake.net2.session.Session;
import com.awake.net2.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author：lqh
 * @Date：2024/3/15 11:28
 */
@ChannelHandler.Sharable
@Component
public class ServerRouterHandler extends BaseRouteHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerRouterHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Session session = Session.valueOf(ctx.channel());
        NetContext.getSessionManager().addServerSession(session);
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
        NetContext.getSessionManager().removeServerSession(session);
        logger.warn("server channel is inactive {}", SessionUtils.sessionSimpleInfo(ctx));
        EventBus.publicEvent(ServerSessionInactiveEvent.valueOf(session));
    }
}
