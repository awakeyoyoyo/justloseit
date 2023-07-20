package com.awake.event.processor;


import com.awake.event.anno.EventReceiver;
import com.awake.event.enhance.EnhanceUtils;
import com.awake.event.enhance.IEventReceiver;
import com.awake.event.manger.EventBus;
import com.awake.event.enhance.EventReceiverDefinition;
import com.awake.event.model.IEvent;
import com.awake.util.ArrayUtils;
import com.awake.util.ReflectionUtils;
import com.awake.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @version : 1.0
 * @ClassName: EventRegisterProcessor
 * @Description: 这是一个后置处理器 在boot项目中注册EventContext时，会import导入EventRegisterProcessor这个组件，
 * @Auther: awake
 * @Date: 2022/9/8 17:03
 **/
@Component
public class EventRegisterProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EventRegisterProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return registerEvent(bean);
    }

    /**
     * 注册被标识方法
     *
     * @param bean
     */
    @SuppressWarnings("unchecked")
    public Object registerEvent(Object bean) {
        Class<?> clazz = bean.getClass();
        Method[] methods = ReflectionUtils.getMethodsByAnnoInPOJOClass(clazz, EventReceiver.class);
        if (ArrayUtils.isEmpty(methods)) {
            return bean;
        }

        if (!ReflectionUtils.isPojoClass(clazz)) {
            logger.warn("The message registration class [{}] is not a POJO class, and the parent class will not be scanned", clazz);
        }

        try {
            for (Method method : methods) {
                Class<?>[] paramClazzs = method.getParameterTypes();
                if (paramClazzs.length != 1) {
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] must have one parameter!", clazz.getName(), method.getName()));
                }
                if (!IEvent.class.isAssignableFrom(paramClazzs[0])) {
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] must have one [IEvent] type parameter!", clazz.getName(), method.getName()));
                }

                Class<? extends IEvent> eventClazz = (Class<? extends IEvent>) paramClazzs[0];
                String eventName = eventClazz.getCanonicalName();
                String methodName = method.getName();

                if (!Modifier.isPublic(method.getModifiers())) {
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] [event:{}] must use 'public' as modifier!", clazz.getName(), methodName, eventName));
                }

                if (Modifier.isStatic(method.getModifiers())) {
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] [event:{}] can not use 'static' as modifier!", clazz.getName(), methodName, eventName));
                }

                String expectedMethodName = StringUtils.format("on{}", eventClazz.getSimpleName());
                if (!methodName.equals(expectedMethodName)) {
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] [event:{}] expects '{}' as method name!"
                            , clazz.getName(), methodName, eventName, expectedMethodName));
                }

                EventReceiverDefinition receiverDefinition = new EventReceiverDefinition(bean, method, eventClazz);
                IEventReceiver enhanceReceiverDefinition = EnhanceUtils.createEventReceiver(receiverDefinition);

                //异步执行标志，false表示同步执行，true表示异步执行
                boolean asyncFlag = method.getDeclaredAnnotation(EventReceiver.class).async();
                // key:class类型 value:观察者 注册Event的receiverMap中
                EventBus.registerEventReceiver(eventClazz, enhanceReceiverDefinition, asyncFlag);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        return bean;
    }
}
