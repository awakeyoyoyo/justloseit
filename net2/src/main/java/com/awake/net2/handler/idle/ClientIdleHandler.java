package com.awake.net2.handler.idle;

import com.awake.net2.module.CommonModule;
import com.awake.net2.packet.Net2Msg;
import com.awake.net2.util.SessionUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author：lqh
 * @Date：2024/3/19 17:08
 */
@ChannelHandler.Sharable
public class ClientIdleHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientIdleHandler.class);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        Net2Msg.CmdPacket cmdPacket = Net2Msg.CmdPacket.newBuilder().setProtoId(CommonModule.Heartbeat)
                .setPacketData(Net2Msg.HeartBeat.newBuilder().build().toByteString()).build();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                logger.warn("client sends heartbeat packet to {}", SessionUtils.sessionSimpleInfo(ctx));
                ctx.channel().writeAndFlush(cmdPacket);
            }
        }
    }
}

