package com.awake.configuration;

import com.awake.net.config.ConfigManager;
import com.awake.net.config.model.NetConfig;
import com.awake.net.protocol.ProtocolManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: ConfigurationTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/13 17:23
 **/

@SpringBootTest(classes = {AppConfiguration.class})
public class ConfigurationTest {

    @Autowired
    private NetConfig netConfig;
    @Autowired
    private ProtocolManager protocolManager;
    @Autowired
    private ConfigManager configManager;
    @Test
    public void configurationTest() {
        System.out.println(netConfig);
        System.out.println(protocolManager);
        System.out.println(configManager);
    }
}
