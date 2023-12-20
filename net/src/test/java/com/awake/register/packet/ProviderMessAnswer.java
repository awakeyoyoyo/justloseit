package com.awake.register.packet;

import com.awake.net.packet.IPacket;
import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ProviderMessAnswer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/25 18:21
 **/
@Packet(protocolId = ProviderMessAnswer.PROTOCOL_ID, moduleId = 3)
@ProtobufClass
@Data
public class ProviderMessAnswer implements IPacket {
    @Ignore
    public static final int PROTOCOL_ID = 1301;
    private String message;

    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }
}
