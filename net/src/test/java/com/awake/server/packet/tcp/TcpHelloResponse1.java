package com.awake.server.packet.tcp;

import com.awake.GameModuleConstant;
import com.awake.net.packet.IPacket;
import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @version : 1.0
 * @ClassName: TcpHelloRequest2
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/6 14:26
 **/
@Packet(protocolId = TcpHelloResponse1.PROTOCOL_ID,moduleId = GameModuleConstant.GAME_MODULE_ID)
@ProtobufClass
public class TcpHelloResponse1 implements IPacket {
    @Ignore
    public static final int PROTOCOL_ID = 3001;

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

