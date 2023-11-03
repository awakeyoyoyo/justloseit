package com.awake.net.session;

import com.awake.net.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: SessionManager
 * @Description: Session管理
 * @Auther: awake
 * @Date: 2023/7/31 10:44
 **/
public class SessionManager implements ISessionManager{

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private static final AtomicInteger SERVER_ATOMIC = new AtomicInteger(0);

    /**
     * 作为服务器，被别的客户端连接的Session
     * 如：自己作为网关，那肯定有一大堆客户端连接，他们连接上来后，就会保存下来这些信息。
     * 因此：要全局消息广播，其实要用这个Map
     */
    private final ConcurrentHashMap<Long, Session> serverSessionMap = new ConcurrentHashMap<>(64);


    /**
     * 作为客户端，连接别的服务器上后，保存下来的Session
     * 如：自己配置了Consumer，说明自己作为消费者将要消费远程接口，就会创建一个TcpClient去连接Provider，那么连接上后，就会保存下来到这个Map中
     */
    private final ConcurrentHashMap<Long, Session> clientSessionMap = new ConcurrentHashMap<>(8);

    /**
     *  服务提供者变化id
     */
    private volatile int serverSessionChangeId = SERVER_ATOMIC.incrementAndGet();

    @Override
    public void addServerSession(Session session) {
        if (serverSessionMap.containsKey(session.getSid())) {
            logger.error("Server received duplicate [session:{}]", SessionUtils.sessionInfo(session));
            return;
        }
        serverSessionMap.put(session.getSid(), session);
        serverSessionChangeId = SERVER_ATOMIC.incrementAndGet();
    }

    @Override
    public void removeServerSession(Session session) {
        if (!serverSessionMap.containsKey(session.getSid())) {
            logger.error("[session:{}] does not exist", SessionUtils.sessionInfo(session));
            return;
        }
        try {
            serverSessionMap.remove(session.getSid());
            session.close();
        } catch (IOException e) {
            logger.error("[session:{}] remove exception", e);
            e.printStackTrace();
        }
        serverSessionChangeId = SERVER_ATOMIC.incrementAndGet();
    }

    @Override
    public Session getServerSession(long sid) {
        return serverSessionMap.get(sid);
    }

    @Override
    public int serverSessionSize() {
        return serverSessionMap.size();
    }

    @Override
    public void forEachServerSession(Consumer<Session> consumer) {
        for (Entry<Long, Session> entry : serverSessionMap.entrySet()) {
            Session session = entry.getValue();
            consumer.accept(session);
        }
    }

    @Override
    public void addClientSession(Session session) {
        if (clientSessionMap.containsKey(session.getSid())) {
            logger.error("client received duplicate [session:{}]", SessionUtils.sessionInfo(session));
            return;
        }
        clientSessionMap.put(session.getSid(), session);
    }

    @Override
    public void removeClientSession(Session session) {
        if (!clientSessionMap.containsKey(session.getSid())) {
            logger.error("[session:{}] does not exist", SessionUtils.sessionInfo(session));
            return;
        }
        try {
            clientSessionMap.remove(session.getSid());
            session.close();
        } catch (IOException e) {
            logger.error("[session:{}] remove exception", e);
            e.printStackTrace();
        }
    }

    @Override
    public Session getClientSession(long sid) {
        return clientSessionMap.get(sid);
    }

    @Override
    public void forEachClientSession(Consumer<Session> consumer) {
        for (Entry<Long, Session> entry : clientSessionMap.entrySet()) {
            Session session = entry.getValue();
            consumer.accept(session);
        }
    }

    @Override
    public int clientSessionSize() {
        return clientSessionMap.size();
    }

    @Override
    public int getClientSessionChangeId() {
        return serverSessionChangeId;
    }

}
