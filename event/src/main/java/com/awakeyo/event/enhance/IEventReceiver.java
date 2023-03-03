package com.awakeyo.event.enhance;

import com.awakeyo.event.model.IEvent;

/**
 * @version : 1.0
 * @ClassName: IEventReceiver
 * @Description: 事件执行接口
 * @Auther: awake
 * @Date: 2022/9/8 16:07
 **/
public interface IEventReceiver {

    void invoke(IEvent event);
}
