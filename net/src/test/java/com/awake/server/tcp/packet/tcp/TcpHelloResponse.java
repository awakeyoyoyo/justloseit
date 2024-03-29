package com.awake.server.tcp.packet.tcp;

import com.awake.net.packet.IPacket;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @version : 1.0
 * @ClassName: TcpHelloResponse
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:18
 **/

@ProtobufClass
public class TcpHelloResponse implements IPacket {
    @Ignore
    public static final int PROTOCOL_ID = 1501;

    private String message;


    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
