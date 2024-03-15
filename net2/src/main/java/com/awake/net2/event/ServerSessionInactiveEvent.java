

package com.awake.net2.event;


import com.awake.event.model.IEvent;
import com.awake.net2.session.Session;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ServerSessionInactiveEvent
 * @Description:
 * @Auther: awake
 * @Date: 2023/7/12 15:27
 **/
@Data
public class ServerSessionInactiveEvent implements IEvent {

    private Session session;

    public static ServerSessionInactiveEvent valueOf(Session session) {
        ServerSessionInactiveEvent event = new ServerSessionInactiveEvent();
        event.session = session;
        return event;
    }

}
