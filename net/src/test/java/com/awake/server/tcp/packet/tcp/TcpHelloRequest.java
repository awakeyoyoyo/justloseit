package com.awake.server.tcp.packet.tcp;

import com.awake.net.packet.IPacket;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @version : 1.0
 * @ClassName: TcpHelloRequest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:18
 **/
@ProtobufClass
public class TcpHelloRequest implements IPacket {
    @Ignore
    public static final int PROTOCOL_ID = 1500;

    private String message;

    public static TcpHelloRequest valueOf(String message) {
        var request = new TcpHelloRequest();
        request.message = message;
        return request;
    }

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

