package com.awake.net.handler;

import com.awake.NetContext;
import com.awake.event.manger.EventBus;
import com.awake.net.event.ClientSessionActiveEvent;
import com.awake.net.event.ClientSessionInactiveEvent;
import com.awake.net.session.Session;
import com.awake.net.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version : 1.0
 * @ClassName: ClientRouteHandler
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/3 20:24
 **/
@ChannelHandler.Sharable
public class ClientRouteHandler extends BaseRouteHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientRouteHandler.class);

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

        NetContext.getNetContext().getSessionManager().removeClientSession(session);
        EventBus.publicEvent(ClientSessionInactiveEvent.valueOf(session));

        // 如果是消费者inactive，还需要触发客户端消费者检查事件，以便重新连接
//        if (session.getProviderAttribute() != null) {
//            NetContext.getNetContext().getConfigManager().getRegistry().checkConsumer();
//        }

        logger.warn("client channel is inactive {}", SessionUtils.sessionSimpleInfo(ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("client session exception caught {}", SessionUtils.sessionSimpleInfo(ctx), cause);
    }

}

