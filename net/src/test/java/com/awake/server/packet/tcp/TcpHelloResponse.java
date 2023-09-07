package com.awake.server.packet.tcp;

import com.awake.net.packet.IPacket;
import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @version : 1.0
 * @ClassName: TcpHelloResponse
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:18
 **/

@Packet(protocolId = TcpHelloResponse.PROTOCOL_ID)
@ProtobufClass
public class TcpHelloResponse implements IPacket {

    public static final short PROTOCOL_ID = 1301;

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
