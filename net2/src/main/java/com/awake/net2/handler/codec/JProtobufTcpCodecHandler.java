package com.awake.net2.handler.codec;

import com.awake.net2.packet.CmdPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @Author：lqh
 * @Date：2024/3/15 11:28
 */
public class JProtobufTcpCodecHandler extends ByteToMessageCodec<CmdPacket> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CmdPacket cmdPacket, ByteBuf byteBuf) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

    }
}
