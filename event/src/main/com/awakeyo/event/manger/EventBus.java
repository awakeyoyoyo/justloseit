package com.awakeyo.event.manger;

import com.awakeyo.event.model.IEvent;
import com.awakeyo.event.enhance.IEventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import java.util.*;


/**
 * @version : 1.0
 * @ClassName: EventBus
 * @Description: 事件分发器
 * @Auther: awake
 * @Date: 2022/9/8 16:05
 **/
public class EventBus {

    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    private static final Map<Class<? extends IEvent>, List<IEventReceiver>> receiverMap = new HashMap<>();


    /**
     * 同步抛出一个事件，会在当前线程中运行
     *
     * @param event 需要抛出的事件
     */
    public static void syncSubmit(IEvent event) {
        List<IEventReceiver> list = receiverMap.get(event.getClass());
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        doSubmit(event, list);
    }

    /**
     * 执行方法调用
     *
     * @param event        事件
     * @param receiverList 所有的观察者
     */
    private static void doSubmit(IEvent event, List<IEventReceiver> receiverList) {
        for (IEventReceiver receiver : receiverList) {
            try {
                receiver.invoke(event);
            } catch (Exception e) {
                logger.error("eventBus未知exception异常", e);
            } catch (Throwable t) {
                logger.error("eventBus未知error异常", t);
            }
        }
    }

    /**
     * 注册事件及其对应观察者
     */
    public static void registerEventReceiver(Class<? extends IEvent> eventType, IEventReceiver receiver) {
        receiverMap.computeIfAbsent(eventType, it -> new ArrayList<>()).add(receiver);
    }

}
