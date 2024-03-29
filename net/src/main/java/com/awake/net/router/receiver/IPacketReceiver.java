package com.awake.net.router.receiver;

import com.awake.net.session.Session;

/**
 * @version : 1.0
 * @ClassName: IPacketReceiver
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/6 11:30
 **/
public interface IPacketReceiver {
    void invoke(Session session, Object packet, Object attachment);
}
