package com.awake.configuration;


import com.awake.net2.protocol.ProtocolManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author：lqh
 * @Date：2024/3/27 14:41
 */
@SpringBootTest(classes = {TestConfiguration.class})
public class ConfigurationTest {


    @Autowired
    private ProtocolManager protocolManager;
    @Test
    public void configurationTest() {
        System.out.println(protocolManager);
    }

}

