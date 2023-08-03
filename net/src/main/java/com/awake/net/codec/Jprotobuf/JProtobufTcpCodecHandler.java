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

import com.awake.net.packet.DecodedPacketInfo;
import com.awake.net.packet.EncodedPacketInfo;
import com.awake.net.packet.IAttachment;
import com.awake.net.packet.IPacket;
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
 * header(4byte) + protocolId(2byte) + packet
 * header = body(bytes.length) + protocolId.length(2byte)
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

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws IOException {
        // 不够读一个int
        if (in.readableBytes() <= PACKET_HEAD_LENGTH) {
            return;
        }
        in.markReaderIndex();
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
        DecodedPacketInfo packetInfo = read(sliceByteBuf);
        out.add(packetInfo);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, EncodedPacketInfo packetInfo, ByteBuf out) throws IOException {
        write(out, packetInfo.getPacket(), packetInfo.getAttachment());
    }

    public static DecodedPacketInfo read(ByteBuf buffer) throws IOException {
        short protocolId = buffer.readShort();
        //协议号获取反序列化方法
//        var protocolRegistration = ProtocolManager.getProtocol(protocolId);
//        var protocolClass = protocolRegistration.protocolConstructor().getDeclaringClass();
        //反序列化
//        var protobufCodec = ProtobufProxy.create(protocolClass);
        //将数据装载
//        var bytes = ByteBufUtils.readAllBytes(buffer);
//        var packet = protobufCodec.decode(bytes);
        return DecodedPacketInfo.valueOf(null, null);
    }

    public void write(ByteBuf buffer, IPacket packet, IAttachment attachment) throws IOException {
        // 写入protobuf协议
        Codec<IPacket> protobufCodec = (Codec<IPacket>) ProtobufProxy.create(packet.getClass());
        byte[] bytes = protobufCodec.encode(packet);
        // header(4byte) + protocolId(2byte)
        buffer.writeInt(bytes.length + 2);

        short protocolId = packet.protocolId();
        // 写入协议号
        buffer.writeShort(protocolId);

        buffer.writeBytes(bytes);
    }
}
