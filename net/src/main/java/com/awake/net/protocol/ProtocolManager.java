package com.awake.net.protocol;

import com.awake.net.protocol.definition.ProtocolDefinition;
import com.awake.net.protocol.properties.ProtocolProperties;
import lombok.Data;

import java.util.HashMap;

/**
 * @version : 1.0
 * @ClassName: ProtocolManager
 * @Description: 协议管理
 * @Auther: awake
 * @Date: 2023/8/3 21:10
 **/
@Data
public class ProtocolManager implements IProtocolManager {

    private ProtocolProperties protocolProperties;

    private HashMap<Integer, ProtocolDefinition> protocolDefinitionHashMap;

    public ProtocolManager(ProtocolProperties protocolProperties) {
        this.protocolProperties=protocolProperties;
    }

    @Override
    public ProtocolDefinition getProtocol(int protocolId) {
        return null;
    }
}
