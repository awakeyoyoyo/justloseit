package com.awake.net2.router;

import com.awake.event.manger.EventBus;
import com.awake.net2.event.ServerExceptionEvent;
import com.awake.net2.packet.common.Heartbeat;
import com.awake.net2.session.Session;
import com.awake.util.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author：lqh
 * @Date：2024/3/15 17:25
 */
public class Router implements IRouter {
    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    @Override
    public void send(Session session, Object packet) {

    }

    @Override
    public void receive(Session session, Object packet) {
        if (packet.getClass() == Heartbeat.class) {
            logger.info("heartbeat");
            return;
        }
        dispatchBySession(session, packet);
    }

    @Override
    public void atReceiver(Session session, Object packet) {
        try {
            // 调用PacketReceiver,进行真正的业务处理,这个submit只是根据packet找到protocolId，然后调用对应的消息处理方法
            // 这个在哪个线程处理取决于：这个上层的PacketReceiverTask被丢到了哪个线程中
            PacketBus.route(session, packet);
        } catch (Exception e) {
            EventBus.publicEvent(ServerExceptionEvent.valueOf(session, packet, e));
            logger.error(StringUtils.format("e[uid:{}][sid:{}] unknown exception", session.getUserId(), session.getSessionId(), e.getMessage()), e);
        } catch (Throwable t) {
            logger.error(StringUtils.format("e[uid:{}][sid:{}] unknown error", session.getUserId(), session.getSessionId(), t.getMessage()), t);
        }
    }

    private void dispatchBySession(Session session, Object packet) {
        long uid = session.getUserId();
        if (uid > 0) {
            TaskBus.execute((int) uid, () -> atReceiver(session, packet));
        } else {
            TaskBus.execute((int) session.getSessionId(), () -> atReceiver(session, packet));
        }
    }

    private void dispatchByTaskExecutorHash(Session session, Object packet, int taskExecutorHash) {
        TaskBus.execute(taskExecutorHash, () -> atReceiver(session, packet));
    }

}
