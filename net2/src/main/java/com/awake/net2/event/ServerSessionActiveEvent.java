

package com.awake.net2.event;


import com.awake.event.model.IEvent;
import com.awake.net2.session.Session;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ServerSessionActiveEvent
 * @Description:
 * @Auther: awake
 * @Date: 2023/7/12 15:27
 **/
@Data
public class ServerSessionActiveEvent implements IEvent {

    private Session session;

    public static ServerSessionActiveEvent valueOf(Session session) {
        ServerSessionActiveEvent event = new ServerSessionActiveEvent();
        event.session = session;
        return event;
    }
}
