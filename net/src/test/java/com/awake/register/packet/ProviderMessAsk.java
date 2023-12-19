package com.awake.register.packet;

import com.awake.net.packet.IPacket;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ProviderMessAsk
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/25 18:18
 **/

@ProtobufClass
@Data
public class ProviderMessAsk implements IPacket {
    @Ignore
    public static final int PROTOCOL_ID = 1300;
    private String message;

    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }
}
