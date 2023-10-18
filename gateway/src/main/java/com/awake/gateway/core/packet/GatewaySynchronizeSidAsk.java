package com.awake.gateway.core.packet;

import com.awake.net.packet.common.ModuleConstant;
import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: GatewaySynchronizeSidAsk
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 17:55
 **/

@Packet(protocolId = GatewaySynchronizeSidAsk.PROTOCOL_ID, moduleId = ModuleConstant.GATEWAY_MODULE_ID)
@ProtobufClass
@Data
public class GatewaySynchronizeSidAsk {
    @Ignore
    public static final short PROTOCOL_ID = 24;
    private String gatewayHostAndPort;

    private Map<Long, Long> sidMap;

    public static GatewaySynchronizeSidAsk valueOf(String gatewayHostAndPort, Map<Long, Long> sidMap) {
        var ask = new GatewaySynchronizeSidAsk();
        ask.gatewayHostAndPort = gatewayHostAndPort;
        ask.sidMap = sidMap;
        return ask;
    }
}
