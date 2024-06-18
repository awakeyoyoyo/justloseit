package com.hello.gamemodule.dailyreset.event;

import com.awake.event.model.IEvent;
import lombok.Data;

/**
 * 系统级别 每分钟事件
 * @Author：lqh
 * @Date：2024/6/18 14:23
 */
@Data
public class OneMinEvent implements IEvent {

    private int hour;
    private int min;

    public static OneMinEvent valueOf(int hour,int min){
        OneMinEvent event=new OneMinEvent();
        event.min=min;
        event.hour=hour;
        return event;
    }
}
