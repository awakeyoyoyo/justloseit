package com.awake.configuration;


import com.awake.net2.protocol.ProtocolManager;
import com.awake.net2.rpc.RpcManager;
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

    @Autowired
    private RpcManager rpcManager;
    @Test
    public void configurationTest() {
        System.out.println(rpcManager);
    }

}

