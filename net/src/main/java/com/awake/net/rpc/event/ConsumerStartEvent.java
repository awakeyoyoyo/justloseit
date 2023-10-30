package com.awake.net.rpc.event;

import com.awake.event.model.IEvent;
import com.awake.net.rpc.registry.RegisterVO;
import com.awake.net.session.Session;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ConsumerStartEvent
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 19:33
 **/
@Data
public class ConsumerStartEvent implements IEvent {
    private RegisterVO registerVO;
    private Session session;

    public static ConsumerStartEvent valueOf(RegisterVO registerVO, Session session) {
        var event = new ConsumerStartEvent();
        event.registerVO = registerVO;
        event.session = session;
        return event;
    }

}
