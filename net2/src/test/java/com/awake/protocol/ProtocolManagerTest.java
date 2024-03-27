package com.awake.protocol;

import com.awake.configuration.TestConfiguration;
import com.awake.net2.protocol.ProtocolManager;
import com.awake.net2.protocol.definition.ProtocolDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Set;

/**
 * @Author：lqh
 * @Date：2024/3/27 16:32
 */
@SpringBootTest(classes = {TestConfiguration.class})
public class ProtocolManagerTest {
    @Autowired
    private ProtocolManager protocolManager;
    @Test
    public void startServer() {
        for (Map.Entry<Integer, ProtocolDefinition> entry : protocolManager.getProtocolDefinitionHashMap().entrySet()) {

            System.out.println("protocolId:" + entry.getKey() + "- classSimpleName:" + entry.getValue().getProtocolClass().getSimpleName());
        }

        for (Map.Entry<Integer, Set<Integer>> entry : protocolManager.getModuleId2ProtocolId().entrySet()) {
            System.out.println("moduleId:" + entry.getKey() + "- protocolIds:" + entry.getValue());
        }
    }

}

