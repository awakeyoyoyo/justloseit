package com.awakeyo.event.shcema;


import com.awakeyo.event.anno.EventReceiver;
import com.awakeyo.event.manger.EventBus;
import com.awakeyo.event.enhance.EventReceiverDefinition;
import com.awakeyo.event.model.IEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * @version : 1.0
 * @ClassName: EventRegisterProcessor
 * @Description: 这是一个后置处理器
 * @Auther: awake
 * @Date: 2022/9/8 17:03
 **/
public class EventRegisterProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EventRegisterProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        registerEvent(bean);
        return bean;
    }

    /**
     * 注册被标识方法
     *
     * @param target
     */
    @SuppressWarnings("unchecked")
    public void registerEvent(Object target) {
        Class<?> clazz = target.getClass();
        Method[] sourceMethods = clazz.getDeclaredMethods();
        for (Method m : sourceMethods) {
            m.setAccessible(true);
            if (m.isAnnotationPresent(EventReceiver.class)) {
                Class<?>[] paramClazzs = m.getParameterTypes();
                if (null == paramClazzs || paramClazzs.length != 1) {
                    logger.error("eventBus注册事件：方法参数中异常");
                    throw new RuntimeException();
                }
                Class<?> paramClazz = paramClazzs[0];
                    if (!paramClazz.isAssignableFrom(IEvent.class)) {
                    logger.error("eventBus注册事件：方法参数不是事件类");
                    throw new RuntimeException();
                }
                Class<? extends IEvent> eventClazz;
                eventClazz = (Class<? extends IEvent>) paramClazz;
                EventReceiverDefinition eventReceiverDefinition = EventReceiverDefinition.valueOf(eventClazz, target, m);
                EventBus.registerEventReceiver(eventClazz, eventReceiverDefinition);
            }
        }
    }
}
