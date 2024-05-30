package com.hello.gamemodule.function;

import com.awake.event.anno.EventReceiver;
import com.awake.event.model.IEvent;
import org.springframework.context.event.EventListener;

/**
 * @Author：lqh
 * @Date：2024/5/28 20:37
 */

public class FunctionOpenEvent implements IEvent {
    public long roleId;
    public int functionId;

}
