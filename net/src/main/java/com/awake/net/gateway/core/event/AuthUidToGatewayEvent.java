package com.awake.net.gateway.core.event;

import com.awake.event.model.IEvent;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;

/**
 * @version : 1.0
 * @ClassName: AuthUidToGatewayEvent
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 17:55
 **/
public class AuthUidToGatewayEvent implements IEvent {
    @Ignore
    public static final short PROTOCOL_ID = 20;
    private long sid;
    private long uid;

    public static AuthUidToGatewayEvent valueOf(long sid, long uid) {
        var event = new AuthUidToGatewayEvent();
        event.sid = sid;
        event.uid = uid;
        return event;
    }
}
