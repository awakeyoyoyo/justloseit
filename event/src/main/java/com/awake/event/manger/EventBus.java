package com.awake.event.manger;

import com.awake.event.model.IEvent;
import com.awake.event.enhance.IEventReceiver;
import com.awake.thread.pool.model.ThreadActorPoolModel;
import com.awake.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /**
     * EN: The size of the thread pool. Event's thread pool is often used to do time-consuming operations, so set it a little bigger
     * CN: 线程池的大小. event的线程池经常用来做一些耗时的操作，所以要设置大一点
     */
    public static final int EXECUTORS_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static final ThreadActorPoolModel executors = new ThreadActorPoolModel(EXECUTORS_SIZE, EventBus.class.getName());

    /**
     * Synchronous event mapping, synchronize observers
     */
    private static final Map<Class<? extends IEvent>, List<IEventReceiver>> receiverMapSync = new HashMap<>();
    /**
     * Asynchronous event mapping, asynchronous observer
     */
    private static final Map<Class<? extends IEvent>, List<IEventReceiver>> receiverMapAsync = new HashMap<>();

    /**
     * 发布事件
     *
     * @param event
     */
    public static void publicEvent(IEvent event) {
        if (event == null) {
            return;
        }
        Class<? extends IEvent> clazz = event.getClass();
        List<IEventReceiver> listSync = receiverMapSync.get(clazz);
        if (!CollectionUtils.isEmpty(listSync)) {
            for (IEventReceiver receiver : listSync) {
                try {
                    receiver.invoke(event);
                } catch (Exception e) {
                    logger.error("eventBus sync event [{}] unknown exception", clazz.getSimpleName(), e);
                } catch (Throwable t) {
                    logger.error("eventBus sync event [{}] unknown error", clazz.getSimpleName(), t);
                }
            }
        }

        List<IEventReceiver> listAsync = receiverMapAsync.get(clazz);
        if (CollectionUtils.isNotEmpty(listAsync)) {
            for (IEventReceiver receiver : listAsync) {
                executors.execute(event.executorHash(), () -> {
                    try {
                        receiver.invoke(event);
                    } catch (Exception e) {
                        logger.error("eventBus async event [{}] unknown exception", clazz.getSimpleName(), e);
                    } catch (Throwable t) {
                        logger.error("eventBus async event [{}] unknown error", clazz.getSimpleName(), t);
                    }
                });
            }
        }
    }


    /**
     * 同步抛出一个事件，会在当前线程中运行
     *
     * @param event 需要抛出的事件
     */
    public static void syncSubmit(IEvent event) {
        List<IEventReceiver> list = receiverMapSync.get(event.getClass());
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
    public static void registerEventReceiver(Class<? extends IEvent> eventType, IEventReceiver receiver, boolean asyncFlag) {
        if (asyncFlag) {
            receiverMapAsync.computeIfAbsent(eventType, it -> new ArrayList<>(1)).add(receiver);
        } else {
            receiverMapSync.computeIfAbsent(eventType, it -> new ArrayList<>(1)).add(receiver);
        }
    }

}
