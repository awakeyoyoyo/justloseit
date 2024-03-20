package com.awake.net2.handler;

import com.awake.event.manger.EventBus;
import com.awake.net2.NetContext;
import com.awake.net2.event.ClientSessionActiveEvent;
import com.awake.net2.event.ClientSessionInactiveEvent;
import com.awake.net2.session.Session;
import com.awake.net2.util.SessionUtils;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author：lqh
 * @Date：2024/3/15 11:28
 */
public class ClientRouterHandler extends BaseRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientRouterHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 客户端的session初始化在启动的时候已经做了，这边直接获取session
        Session session = SessionUtils.getSession(ctx);
        EventBus.publicEvent(ClientSessionActiveEvent.valueOf(session));
        logger.info("client channel is active {}", SessionUtils.sessionInfo(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        Session session = SessionUtils.getSession(ctx);

        if (session == null) {
            return;
        }

        NetContext.getSessionManager().removeClientSession(session);
        EventBus.publicEvent(ClientSessionInactiveEvent.valueOf(session));

        logger.warn("client channel is inactive {}", SessionUtils.sessionSimpleInfo(ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("client session exception caught {}", SessionUtils.sessionSimpleInfo(ctx), cause);
    }
}
