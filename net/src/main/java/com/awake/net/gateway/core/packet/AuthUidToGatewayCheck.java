package com.awake.net.gateway.core.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: AuthUidToGatewayCheck
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 17:55
 **/

@ProtobufClass
@Data
public class AuthUidToGatewayCheck {

    @Ignore
    public static final short PROTOCOL_ID = 20;

    private long uid;

    public static AuthUidToGatewayCheck valueOf(long uid) {
        var authUidToGateway = new AuthUidToGatewayCheck();
        authUidToGateway.uid = uid;
        return authUidToGateway;
    }
}
