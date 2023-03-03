package com.awakeyo.event.enhance;

import com.awakeyo.event.model.IEvent;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @version : 1.0
 * @ClassName: EventReceiverDefinition
 * @Description: 事件执行者
 * @Auther: awake
 * @Date: 2022/9/8 16:10
 **/
public class EventReceiverDefinition implements IEventReceiver {

    private Object bean;

    private Method method;

    private Class<? extends IEvent> eventClazz;

    public EventReceiverDefinition(Object bean, Method method, Class<? extends IEvent> eventClazz) {
        this.bean = bean;
        this.method = method;
        this.eventClazz = eventClazz;
        ReflectionUtils.makeAccessible(this.method);
    }

    public static EventReceiverDefinition valueOf(Class<? extends IEvent> paramClass, Object target, Method m) {
        return new EventReceiverDefinition(target, m, paramClass);
    }

    @Override
    public void invoke(IEvent event) {
        ReflectionUtils.invokeMethod(method, bean, event);
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<? extends IEvent> getEventClazz() {
        return eventClazz;
    }

    public void setEventClazz(Class<? extends IEvent> eventClazz) {
        this.eventClazz = eventClazz;
    }
}
