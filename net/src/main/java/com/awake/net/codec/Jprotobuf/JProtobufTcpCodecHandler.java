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

package com.awake.net.codec.Jprotobuf;

import com.awake.ProtocolContext;
import com.awake.net.packet.DecodedPacketInfo;
import com.awake.net.packet.EncodedPacketInfo;
import com.awake.net.packet.IPacket;
import com.awake.protocol.definition.ProtocolDefinition;
import com.awake.util.IOUtils;
import com.awake.util.StringUtils;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.IOException;
import java.util.List;

/**
 * 包结构
 * {
 * header(4byte) {len}
 * protocolId(4byte)
 * attachment.length(int)
 * attachmentProtocolId(4byte)
 * packet
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
    /**
     * 包体的头部的长度，一个int字节长度
     */
    public static final int PACKET_HEAD_LENGTH = 4;

    /**
     * 包协议号长度，一个int字节长度
     */
    public static final int PROTOCOL_ID_LENGTH = 4;

    /**
     * 包协议号长度，一个int字节长度
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
        int attachmentLen = sliceByteBuf.readInt();
        //附加包id
        int attachmentProtocolId = 0;
        if (attachmentLen != 0) {
            attachmentProtocolId = sliceByteBuf.readInt();
        }
        //协议包
        ProtocolDefinition packetDefinition = ProtocolContext.getProtocolContext().getProtocolManager().getProtocol(packetProtocolId);
        Codec packetCodec = ProtobufProxy.create(packetDefinition.getProtocolClass());
        int readableBytes = sliceByteBuf.readableBytes();
        byte[] packetBytes = new byte[readableBytes - attachmentLen];
        sliceByteBuf.readBytes(packetBytes);
        Object packet = packetCodec.decode(packetBytes);


        //附加包
        IAttachment attachment = null;
        if (attachmentProtocolId != 0) {
            ProtocolDefinition attachmentDefinition = ProtocolContext.getProtocolContext().getProtocolManager().getProtocol(attachmentProtocolId);
            Codec attachmentCodec = ProtobufProxy.create(attachmentDefinition.getProtocolClass());
            byte[] attachmentBytes = new byte[readableBytes - attachmentLen];
            sliceByteBuf.readBytes(attachmentBytes);
            attachment = (IAttachment) attachmentCodec.decode(attachmentBytes);
        }

        
        DecodedPacketInfo decodedPacketInfo = DecodedPacketInfo.valueOf((IPacket) packet, attachment);
        out.add(decodedPacketInfo);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, EncodedPacketInfo packetInfo, ByteBuf out) throws IOException {
        IPacket packet = packetInfo.getPacket();
        IAttachment attachment = packetInfo.getAttachment();
        int len = 0;
        // 写入protobuf协议
        Codec<IPacket> packetCodec = (Codec<IPacket>) ProtobufProxy.create(packet.getClass());
        byte[] packetBytes = packetCodec.encode(packet);
        len += packetBytes.length + PROTOCOL_ID_LENGTH;

        byte[] attachmentBytes = null;
        int attachmentProtocolId = 0;
        if (attachment != null) {
            Codec<IAttachment> attachmentCodec = (Codec<IAttachment>) ProtobufProxy.create(attachment.getClass());
            attachmentBytes = attachmentCodec.encode(attachment);
            attachmentProtocolId = attachment.protocolId();
            len += attachmentBytes.length + PROTOCOL_ID_LENGTH + ATTACHMENT_LENGTH_LEN;
        }


        // 包头 包长度
        out.writeInt(len);
        int packetProtocolId = packet.protocolId();
        // 协议号
        out.writeInt(packetProtocolId);
        // 附加包长度
        out.writeInt(attachmentBytes == null ? 0 : attachmentBytes.length);
        if (attachmentBytes != null) {
            //附加包协议号
            out.writeInt(attachmentProtocolId);
        }
        // 协议包
        out.writeBytes(packetBytes);
        if (attachmentBytes != null) {
            //附加包
            out.writeBytes(attachmentBytes);
        }
    }
}
