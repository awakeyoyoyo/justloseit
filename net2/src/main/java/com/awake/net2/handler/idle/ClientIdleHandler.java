package com.awake.net2.handler.idle;

import com.awake.net2.packet.CmdPacket;
import com.awake.net2.packet.common.Heartbeat;
import com.awake.net2.util.SessionUtils;
import com.awake.util.IOUtils;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author：lqh
 * @Date：2024/3/19 17:08
 */
@ChannelHandler.Sharable
public class ClientIdleHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientIdleHandler.class);
    private static final Codec cmdPacketCodec = ProtobufProxy.create(CmdPacket.class);
    private static final Heartbeat heartbeat = new Heartbeat();
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws IOException {
        CmdPacket cmdPacket = CmdPacket.valueOf(Heartbeat.PROTOCOL_ID, cmdPacketCodec.encode(heartbeat));
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                logger.warn("client sends heartbeat packet to {}", SessionUtils.sessionSimpleInfo(ctx));
                ctx.channel().writeAndFlush(cmdPacket);
            }
        }
    }
}

