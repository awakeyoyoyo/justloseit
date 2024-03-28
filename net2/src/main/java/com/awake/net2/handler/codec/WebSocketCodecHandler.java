package com.awake.net2.handler.codec;

import com.awake.net2.packet.CmdPacket;
import com.awake.util.IOUtils;
import com.awake.util.base.StringUtils;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

/**
 * len(int)+data
 *
 * @Author：lqh
 * @Date：2024/3/27 16:17
 */
public class WebSocketCodecHandler extends MessageToMessageCodec<WebSocketFrame, CmdPacket> {
    /**
     * 包体的头部的长度，一个int字节长度
     */
    public static final int PACKET_HEAD_LENGTH = 4;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CmdPacket cmdPacket, List<Object> list) throws Exception {
        var byteBuf = channelHandlerContext.alloc().ioBuffer();
        //包头默认长度
        Codec packetCodec = ProtobufProxy.create(CmdPacket.class);
        byte[] packetBytes = packetCodec.encode(cmdPacket);
        // 主包長度+協議號
        int packetLength = packetBytes.length;

        // 包头 包长度
        byteBuf.writeInt(packetLength);
        // 通信包
        byteBuf.writeBytes(packetBytes);

        list.add(new BinaryWebSocketFrame(byteBuf));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {
        ByteBuf in = webSocketFrame.content();
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
        Codec cmdPacketCodec = ProtobufProxy.create(CmdPacket.class);
        CmdPacket cmdPacket = (CmdPacket) cmdPacketCodec.decode(packetBytes);
        list.add(cmdPacket);
    }
}
