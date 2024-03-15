package com.awake.net2.protocol;

import com.awake.net2.protocol.definition.ProtocolDefinition;

/**
 * @Author：lqh
 * @Date：2024/3/15 14:10
 */
public interface IProtocolManager {

    /**
     * 根据协议号获取proto信息类
     * @param protocolId
     * @return
     */
    ProtocolDefinition getProtocolDefinition(int protocolId);

    int getProtocolId(Class<?> packetClazz);
}
