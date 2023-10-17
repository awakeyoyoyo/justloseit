package com.awake.net.session;

import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: ISessionManager
 * @Description: Session管理
 * @Auther: awake
 * @Date: 2023/7/31 10:44
 **/
public interface ISessionManager {

    /**
     * 增加服务session
     * @param session
     */
    void addServerSession(Session session);

    /**
     * 删除服务session
     * @param session
     */
    void removeServerSession(Session session);

    /**
     * 获取服务session
     * @param id
     * @return
     */
    Session getServerSession(long id);

    /**
     * 提供服务的 服务session数目
     * @return
     */
    int serverSessionSize();

    /**
     * 遍历提供服务的session
     * @param consumer
     */
    void forEachServerSession(Consumer<Session> consumer);

    /**
     * 增加客户端session
     * @param session
     */
    void addClientSession(Session session);

    /**
     * 删除客户端session
     * @param session
     */
    void removeClientSession(Session session);

    /**
     * 获取客户端session
     * @param id
     * @return
     */
    Session getClientSession(long id);

    /**
     * 遍历客户端session
     * @param consumer
     */
    void forEachClientSession(Consumer<Session> consumer);

    /**
     * 遍历客户端数目
     * @return
     */
    int clientSessionSize();

    int getClientSessionChangeId();
}
