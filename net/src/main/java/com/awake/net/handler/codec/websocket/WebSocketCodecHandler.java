package com.awake.net.handler.codec.websocket;

import com.awake.NetContext;
import com.awake.net.packet.DecodedPacketInfo;
import com.awake.net.packet.EncodedPacketInfo;
import com.awake.net.protocol.definition.ProtocolDefinition;
import com.awake.net.router.attachment.IAttachment;
import com.awake.util.IOUtils;
import com.awake.util.base.StringUtils;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.io.IOException;
import java.util.List;

import static com.awake.net.handler.codec.Jprotobuf.JProtobufTcpCodecHandler.*;

/**
 * @version : 1.0
 * @ClassName: WebSocketCodecHandler
 * @Description:
 *
 *  * 包结构
 *  * {
 *  * header(4byte)    { len }
 *  * protocolId(4byte)
 *  * attachment.length(4byte)
 *  * packet
 *  * //可能沒有
 *  * attachmentProtocolId(4byte)
 *  * attachment
 *  * }
 *  * header = body(bytes.length) + protocolId.length(4byte) + attachment(attachment.length)
 *
 * @Auther: awake
 * @Date: 2023/12/13 17:29
 **/
public class WebSocketCodecHandler extends MessageToMessageCodec<WebSocketFrame, EncodedPacketInfo> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws IOException {
        ByteBuf in = webSocketFrame.content();
        var length = in.readInt();
        // 如果长度非法，则抛出异常断开连接，按照自己的使用场景指定合适的长度，防止客户端发送超大包占用带宽
        if (length < 0 || length > IOUtils.BYTES_PER_MB) {
            throw new IllegalArgumentException(StringUtils.format("illegal packet [length:{}]", length));
        }
        var sliceByteBuf = in.readSlice(length);
        //协议包id
        int packetProtocolId = sliceByteBuf.readInt();
        //附加包长度
        int attachmentLength = sliceByteBuf.readInt();

        Object packet;
        Object attachment = null;
        ProtocolDefinition packetDefinition = NetContext.getProtocolManager().getProtocolDefinition(packetProtocolId);
        if (packetDefinition==null){
            throw new IllegalArgumentException(StringUtils.format("illegal packetProtocolId no register [packetProtocolId:{}]", packetProtocolId));
        }
        Codec packetCodec = ProtobufProxy.create(packetDefinition.getProtocolClass());
        int readableBytes = sliceByteBuf.readableBytes();
        if (attachmentLength != 0) {
            //协议包
            byte[] packetBytes = new byte[readableBytes - ATTACHMENT_LENGTH_LEN - attachmentLength];
            sliceByteBuf.readBytes(packetBytes);
            packet = packetCodec.decode(packetBytes);
            //附加包id
            int attachmentProtocolId = sliceByteBuf.readInt();
            //附加包
            ProtocolDefinition attachmentDefinition = NetContext.getProtocolManager().getProtocolDefinition(attachmentProtocolId);
            Codec attachmentCodec = ProtobufProxy.create(attachmentDefinition.getProtocolClass());
            byte[] attachmentBytes = new byte[attachmentLength];
            sliceByteBuf.readBytes(attachmentBytes);
            attachment = attachmentCodec.decode(attachmentBytes);
        }else{
            //协议包
            byte[] packetBytes = new byte[readableBytes];
            sliceByteBuf.readBytes(packetBytes);
            packet = packetCodec.decode(packetBytes);
        }
        DecodedPacketInfo decodedPacketInfo = DecodedPacketInfo.valueOf(packet, attachment);
        list.add(decodedPacketInfo);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, EncodedPacketInfo packetInfo, List<Object> list) throws IOException {
        var byteBuf = channelHandlerContext.alloc().ioBuffer();
        Object packet = packetInfo.getPacket();
        int protocolId = NetContext.getProtocolManager().getProtocolId(packet.getClass());
        Object attachmentObj = packetInfo.getAttachment();
        //默認有包頭長度4byte
        int packetLength = PACKET_HEAD_LENGTH;
        Codec packetCodec = ProtobufProxy.create(packet.getClass());
        byte[] packetBytes = packetCodec.encode(packet);
        // 主包長度+協議號
        packetLength += packetBytes.length + PROTOCOL_ID_LENGTH;

        byte[] attachmentBytes = null;
        int attachmentProtocolId = 0;
        int attachmentLength = 0;
        if (attachmentObj != null) {
            IAttachment iAttachment = (IAttachment) attachmentObj;
            Codec<IAttachment> attachmentCodec = (Codec<IAttachment>) ProtobufProxy.create(attachmentObj.getClass());
            attachmentBytes = attachmentCodec.encode(iAttachment);
            attachmentLength = attachmentBytes.length;
            attachmentProtocolId = iAttachment.protocolId();
            packetLength += attachmentLength + ATTACHMENT_LENGTH_LEN;
        }
        // 包头 包长度
        byteBuf.writeInt(packetLength);
        // 协议号
        byteBuf.writeInt(protocolId);
        // 附加包长度
        byteBuf.writeInt(attachmentLength);
        // 协议包
        byteBuf.writeBytes(packetBytes);
        if (attachmentLength != 0) {
            //附加包协议号
            byteBuf.writeInt(attachmentProtocolId);
            //附加包
            byteBuf.writeBytes(attachmentBytes);
        }
        list.add(new BinaryWebSocketFrame(byteBuf));
    }


}

