package com.awake.protocol;

import com.awake.NetContext;
import com.awake.net.protocol.ProtocolManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: ProtocolTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/4 16:13
 **/


@SpringBootTest(classes = {ApplicationConfiguration.class})
public class ProtocolTest {
    @Autowired
    private ApplicationConfiguration applicationConfiguration;
    @Autowired
    private NetContext netContext;
    @Autowired
    private ProtocolManager protocolManager;
    @Test
    public void testProtocol() {
        System.out.println(protocolManager);
    }

}
