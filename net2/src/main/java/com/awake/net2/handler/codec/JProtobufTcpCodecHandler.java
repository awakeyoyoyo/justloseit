package com.awake.net2.handler.codec;

import com.awake.net2.packet.Net2Msg;
import com.awake.util.IOUtils;
import com.awake.util.base.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * len(int)+data
 * @Author：lqh
 * @Date：2024/3/15 11:28
 */
public class JProtobufTcpCodecHandler extends ByteToMessageCodec<Net2Msg.CmdPacket> {

    /**
     * 包体的头部的长度，一个int字节长度
     */
    public static final int PACKET_HEAD_LENGTH = 4;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Net2Msg.CmdPacket cmdPacket, ByteBuf out) throws Exception {
        //包头默认长度
        byte[] packetBytes = cmdPacket.toByteArray();
        // 主包長度+協議號
        int packetLength = packetBytes.length;

        // 包头 包长度
        out.writeInt(packetLength);
        // 通信包
        out.writeBytes(packetBytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 不够读一个int
        if (in.readableBytes() <= PACKET_HEAD_LENGTH) {
            return;
        }
        in.markReaderIndex();
        // 包头 包长度
        int length = in.readInt();

        // 如果长度非法，则抛出异常断开连接，按照自己的使用场景指定合适的长度，防止客户端发送超大包占用带宽
        if (length < 0 || length > IOUtils.BYTES_PER_MB) {
            throw new IllegalArgumentException(StringUtils.format("illegal packet [length:{}]", length));
        }

        // ByteBuf里的数据太小
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf sliceByteBuf = in.readSlice(length);
        int readableBytes = sliceByteBuf.readableBytes();
        byte[] packetBytes = new byte[readableBytes];
        sliceByteBuf.readBytes(packetBytes);
        Net2Msg.CmdPacket cmdPacket = Net2Msg.CmdPacket.parseFrom(packetBytes);
        out.add(cmdPacket);
    }
}
