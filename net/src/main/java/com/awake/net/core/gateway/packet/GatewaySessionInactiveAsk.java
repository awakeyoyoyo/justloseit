package com.awake.net.core.gateway.packet;

import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: GatewaySessionInactiveAsk
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 17:55
 **/

@Packet(protocolId = GatewaySessionInactiveAsk.PROTOCOL_ID)
@ProtobufClass
@Data
public class GatewaySessionInactiveAsk {
    @Ignore
    public static final short PROTOCOL_ID = 23;
    private String gatewayHostAndPort;

    private long sid;
    private long uid;

    public static GatewaySessionInactiveAsk valueOf(String gatewayHostAndPort, long sid, long uid) {
        var ask = new GatewaySessionInactiveAsk();
        ask.gatewayHostAndPort = gatewayHostAndPort;
        ask.sid = sid;
        ask.uid = uid;
        return ask;
    }
}
