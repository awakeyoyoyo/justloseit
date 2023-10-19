package com.awake.net.gateway.core.event;

import com.awake.event.model.IEvent;

/**
 * @version : 1.0
 * @ClassName: GatewaySessionInactiveEvent
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 17:55
 **/
public class GatewaySessionInactiveEvent implements IEvent {

    private long sid;
    private long uid;

    public static GatewaySessionInactiveEvent valueOf(long sid, long uid) {
        var event = new GatewaySessionInactiveEvent();
        event.sid = sid;
        event.uid = uid;
        return event;
    }
}
