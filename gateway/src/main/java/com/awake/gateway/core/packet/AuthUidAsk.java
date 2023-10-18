package com.awake.gateway.core.packet;

import com.awake.net.packet.common.ModuleConstant;
import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: AuthUidAsk
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 17:55
 **/

@Packet(protocolId = AuthUidAsk.PROTOCOL_ID, moduleId = ModuleConstant.GATEWAY_MODULE_ID)
@ProtobufClass
@Data
public class AuthUidAsk {
    @Ignore
    public static final short PROTOCOL_ID = 22;

    private String gatewayHostAndPort;

    private long sid;
    private long uid;

    public static AuthUidAsk valueOf(String gatewayHostAndPort, long sid, long uid) {
        var ask = new AuthUidAsk();
        ask.gatewayHostAndPort = gatewayHostAndPort;
        ask.sid = sid;
        ask.uid = uid;
        return ask;
    }
}
