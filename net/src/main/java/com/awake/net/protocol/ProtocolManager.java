package com.awake.net.protocol;

import com.awake.net.protocol.definition.ProtocolDefinition;

import java.util.HashMap;

/**
 * @version : 1.0
 * @ClassName: ProtocolManager
 * @Description: 协议管理
 * @Auther: awake
 * @Date: 2023/8/3 21:10
 **/
public class ProtocolManager implements IProtocolManager {

    private HashMap<Integer, ProtocolDefinition> protocolDefinitionHashMap;

    @Override
    public ProtocolDefinition getProtocol(int protocolId) {
        return null;
    }
}
