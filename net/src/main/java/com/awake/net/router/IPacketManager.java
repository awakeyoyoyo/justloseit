package com.awake.net.router;


import com.awake.net.router.receiver.IPacketReceiver;
import com.awake.net.router.receiver.ProtocolDefinition;
import org.springframework.context.ApplicationContext;

/**
 * @version : 1.0
 * @ClassName: IPacketManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/19 14:40
 **/
public interface IPacketManager {

    /**
     * 根据协议号获取proto信息类
     * @param protocolId
     * @return
     */
    ProtocolDefinition getProtocolDefinition(int protocolId);

    int getProtocolId(Class<?> packetClazz);

    void init(ApplicationContext applicationContext);

    IPacketReceiver getPacketReceiver(Object packet);
}
