package com.awake.net.provider.event;

import com.awake.net.provider.registry.RegisterVO;
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
public class ConsumerStartEvent {
    private RegisterVO registerVO;
    private Session session;

    public static ConsumerStartEvent valueOf(RegisterVO registerVO, Session session) {
        var event = new ConsumerStartEvent();
        event.registerVO = registerVO;
        event.session = session;
        return event;
    }

}
