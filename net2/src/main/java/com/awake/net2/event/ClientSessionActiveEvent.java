
package com.awake.net2.event;

import com.awake.event.model.IEvent;

import com.awake.net2.session.Session;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ClientSessionActiveEvent
 * @Description:
 * @Auther: awake
 * @Date: 2023/7/12 15:27
 **/
@Data
public class ClientSessionActiveEvent implements IEvent {

    private Session session;

    public static ClientSessionActiveEvent valueOf(Session session) {
        ClientSessionActiveEvent event = new ClientSessionActiveEvent();
        event.session = session;
        return event;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
