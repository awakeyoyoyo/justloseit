package com.awake.protocol;

import com.awake.protocol.definition.ProtocolDefinition;
import com.awake.protocol.properties.ProtocolProperties;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;

/**
 * @version : 1.0
 * @ClassName: ProtocolManager
 * @Description: 协议管理
 * @Auther: awake
 * @Date: 2023/8/3 21:10
 **/
@Data
public class ProtocolManager implements IProtocolManager, InitializingBean {

    private ProtocolProperties protocolProperties;

    private HashMap<Integer, ProtocolDefinition> protocolDefinitionHashMap;

    public ProtocolManager(ProtocolProperties protocolProperties) {
        this.protocolProperties=protocolProperties;
    }

    @Override
    public ProtocolDefinition getProtocol(int protocolId) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String scanProtocolPacket = protocolProperties.getScanProtocolPacket();
        //TODO 扫描出所有协议包
    }
}
