package com.awake.net.protocol;


import com.awake.net.protocol.definition.ProtocolDefinition;

/**
 * @version : 1.0
 * @ClassName: IProtocolManager
 * @Description: 协议管理接口
 * @Auther: awake
 * @Date: 2023/8/3 21:09
 **/
public interface IProtocolManager {

    /**
     * 根据协议号获取proto信息类
     * @param protocolId
     * @return
     */
    ProtocolDefinition getProtocolDefinition(int protocolId);


    int getProtocolId(Class<?> packetClazz);
}
