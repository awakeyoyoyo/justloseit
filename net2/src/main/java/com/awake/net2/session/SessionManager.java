package com.awake.net2.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    }

    @Override
    public void removeServerSession(Session session) {

    }

    @Override
    public Session getServerSession(long id) {
        return null;
    }

    @Override
    public int serverSessionSize() {
        return 0;
    }

    @Override
    public void forEachServerSession(Consumer<Session> consumer) {

    }

    @Override
    public void addClientSession(Session session) {

    }

    @Override
    public void removeClientSession(Session session) {

    }

    @Override
    public Session getClientSession(long id) {
        return null;
    }

    @Override
    public void forEachClientSession(Consumer<Session> consumer) {

    }

    @Override
    public int clientSessionSize() {
        return 0;
    }
}
