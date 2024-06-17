package com.hello.gamemodule.event;

import com.awake.event.model.IEvent;
import com.awake.net2.session.Session;

/**
 * @Author：lqh
 * @Date：2024/6/17 20:36
 */
public class LoginEvent  implements IEvent {
    private Session session;

    public static LoginEvent valueOf(Session session) {
        LoginEvent event = new LoginEvent();
        event.session = session;
        return event;
    }

}

