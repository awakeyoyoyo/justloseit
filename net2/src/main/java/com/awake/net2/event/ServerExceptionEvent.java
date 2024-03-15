

package com.awake.net2.event;


import com.awake.event.model.IEvent;
import com.awake.net2.session.Session;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ServerExceptionEvent
 * @Description:
 * @Auther: awake
 * @Date: 2023/7/12 15:27
 **/
@Data
public class ServerExceptionEvent implements IEvent {

    private Session session;
    private Object packet;
    private Exception exception;

    public static ServerExceptionEvent valueOf(Session session, Object packet, Exception exception) {
        ServerExceptionEvent event = new ServerExceptionEvent();
        event.session = session;
        event.packet = packet;
        event.exception = exception;
        return event;
    }

}
