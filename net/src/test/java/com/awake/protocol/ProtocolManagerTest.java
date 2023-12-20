package com.awake.protocol;

import com.awake.net.protocol.ProtocolManager;
import com.awake.net.protocol.definition.ProtocolDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map.Entry;

/**
 * @version : 1.0
 * @ClassName: ProtocolManagerTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/9 20:52
 **/
@SpringBootTest(classes = {ApplicationConfiguration.class})
public class ProtocolManagerTest {
    @Autowired
    private ProtocolManager protocolManager;
    @Test
    public void startServer() {
        for (Entry<Integer, ProtocolDefinition> entry : protocolManager.getProtocolDefinitionHashMap().entrySet()) {

            System.out.println("protocolId:"+entry.getKey()+"- classSimpleName:"+entry.getValue().getProtocolClass().getSimpleName());
        }
    }

}
