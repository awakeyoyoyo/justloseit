package com.awake.net2.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/3/15 10:41
 */
@ProtobufClass
@Data
public class CmdPacket {
    private int protoId;
    private byte[] packetData;
}
