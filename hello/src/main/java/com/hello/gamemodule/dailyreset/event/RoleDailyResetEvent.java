package com.hello.gamemodule.dailyreset.event;

import com.awake.event.model.IEvent;
import com.awake.net2.session.Session;
import lombok.Data;

/**
 * 玩家每日更新事件
 * @Author：lqh
 * @Date：2024/6/18 14:40
 */
@Data
public class RoleDailyResetEvent implements IEvent {
    private Session session;

    public static RoleDailyResetEvent valueOf(Session session){
        RoleDailyResetEvent event=new RoleDailyResetEvent();
        event.session =session;
        return event;
    }

}
