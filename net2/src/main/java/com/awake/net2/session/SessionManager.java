package com.awake.net2.session;

import com.awake.net2.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Author：lqh
 * @Date：2024/3/15 10:58
 */
public class SessionManager implements ISessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    /**
     * 作为服务器，被别的客户端连接的Session
     * 如：自己作为网关，那肯定有一大堆客户端连接，他们连接上来后，就会保存下来这些信息。
     * 因此：要全局消息广播，其实要用这个Map
     */
    private final ConcurrentHashMap<Long, Session> serverSessionMap = new ConcurrentHashMap<>(64);


    /**
     * 作为客户端，连接别的服务器上后，保存下来的Session
     */
    private final ConcurrentHashMap<Long, Session> clientSessionMap = new ConcurrentHashMap<>(8);


    @Override
    public void addServerSession(Session session) {
        if (serverSessionMap.containsKey(session.getSessionId())) {
            logger.error("Server received duplicate [session:{}]", SessionUtils.sessionInfo(session));
            return;
        }
        serverSessionMap.put(session.getSessionId(), session);
    }

    @Override
    public void removeServerSession(Session session) {
        if (!serverSessionMap.containsKey(session.getSessionId())) {
            logger.error("[session:{}] does not exist", SessionUtils.sessionInfo(session));
            return;
        }
        try {
            serverSessionMap.remove(session.getSessionId());
            session.close();
        } catch (Exception e) {
            logger.error("[session:{}] remove exception", e);
            e.printStackTrace();
        }
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
        for (Map.Entry<Long, Session> entry : serverSessionMap.entrySet()) {
            Session session = entry.getValue();
            consumer.accept(session);
        }
    }

    @Override
    public void addClientSession(Session session) {
        if (clientSessionMap.containsKey(session.getSessionId())) {
            logger.error("client received duplicate [session:{}]", SessionUtils.sessionInfo(session));
            return;
        }
        clientSessionMap.put(session.getSessionId(), session);
    }

    @Override
    public void removeClientSession(Session session) {
        if (!clientSessionMap.containsKey(session.getSessionId())) {
            logger.error("[session:{}] does not exist", SessionUtils.sessionInfo(session));
            return;
        }
        try {
            clientSessionMap.remove(session.getSessionId());
            session.close();
        } catch (Exception e) {
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
        for (Map.Entry<Long, Session> entry : clientSessionMap.entrySet()) {
            Session session = entry.getValue();
            consumer.accept(session);
        }
    }

    @Override
    public int clientSessionSize() {
        return clientSessionMap.size();
    }
}
