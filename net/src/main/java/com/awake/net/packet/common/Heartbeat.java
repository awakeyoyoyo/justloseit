package com.awake.net.packet.common;

import com.awake.net.packet.IPacket;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import jdk.nashorn.internal.ir.annotations.Ignore;

/**
 * @version : 1.0
 * @ClassName: Heartbeat
 * @Description: 心跳包
 * @Auther: awake
 * @Date: 2023/8/3 20:38
 **/
@ProtobufClass
public class Heartbeat implements IPacket {

    @Ignore
    public static final short PROTOCOL_ID = 102;

    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }

}
