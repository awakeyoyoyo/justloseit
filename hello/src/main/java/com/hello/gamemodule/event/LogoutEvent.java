package com.hello.gamemodule.event;

import com.awake.event.model.IEvent;
import com.awake.net2.event.ServerSessionInactiveEvent;
import com.awake.net2.session.Session;

/**
 * 登出事件
 * @Author：lqh
 * @Date：2024/6/17 20:28
 */
public class LogoutEvent  implements IEvent {
    private Session session;

    public static LogoutEvent valueOf(Session session) {
        LogoutEvent event = new LogoutEvent();
        event.session = session;
        return event;
    }

}
