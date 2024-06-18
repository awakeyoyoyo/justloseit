package com.hello.gamemodule.dailyreset.event;

import com.awake.event.model.IEvent;

/**
 * 服务器级别的每日重置事件
 * @Author：lqh
 * @Date：2024/6/18 14:39
 */
public class DailyResetEvent implements IEvent {

    public static DailyResetEvent valueOf(){
        DailyResetEvent event=new DailyResetEvent();
        return event;
    }
}
