/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.awake.net.handler.codec.Jprotobuf;

import com.awake.NetContext;
import com.awake.net.packet.DecodedPacketInfo;
import com.awake.net.packet.EncodedPacketInfo;
import com.awake.net.router.attachment.IAttachment;
import com.awake.net.router.receiver.ProtocolDefinition;
import com.awake.util.IOUtils;
import com.awake.util.base.StringUtils;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 包结构
 * {
 * header(4byte)    { len }
 * protocolId(4byte)
 * attachment.length(4byte)
 * packet
 * //可能沒有
 * attachmentProtocolId(4byte)
 * attachment
 * }
 * header = body(bytes.length) + protocolId.length(4byte) + attachment(attachment.length)
 *
 * @version : 1.0
 * @ClassName: AbstractClient
 * @Description: 抽象客户端
 * @Auther: awake
 * @Date: 2023/8/2 20:12
 **/
public class JProtobufTcpCodecHandler extends ByteToMessageCodec<EncodedPacketInfo> {

    private static final Logger logger = LoggerFactory.getLogger(JProtobufTcpCodecHandler.class);
    /**
     * 包体的头部的长度，一个int字节长度
     */
    public static final int PACKET_HEAD_LENGTH = 4;

    /**
     * 主包协议号长度，一个int字节长度
     */
    public static final int PROTOCOL_ID_LENGTH = 4;

    /**
     * 信號包协议号长度，一个int字节长度
     */
    public static final int ATTACHMENT_LENGTH_LEN = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws IOException {
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
        //协议包id
        int packetProtocolId = sliceByteBuf.readInt();
        //附加包长度
        int attachmentLength = sliceByteBuf.readInt();

        Object packet;
        Object attachment = null;
        ProtocolDefinition packetDefinition = NetContext.getPacketManager().getProtocolDefinition(packetProtocolId);
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
            ProtocolDefinition attachmentDefinition = NetContext.getPacketManager().getProtocolDefinition(attachmentProtocolId);
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
        out.add(decodedPacketInfo);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, EncodedPacketInfo packetInfo, ByteBuf out) throws IOException {
        Object packet = packetInfo.getPacket();
        int protocolId = NetContext.getPacketManager().getProtocolId(packet.getClass());
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
        out.writeInt(packetLength);
        // 协议号
        out.writeInt(protocolId);
        // 附加包长度
        out.writeInt(attachmentLength);
        // 协议包
        out.writeBytes(packetBytes);
        if (attachmentLength != 0) {
            //附加包协议号
            out.writeInt(attachmentProtocolId);
            //附加包
            out.writeBytes(attachmentBytes);
        }
    }
}
