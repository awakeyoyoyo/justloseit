package com.awake.server.tcp.client;

import com.awake.configuration.TestConfiguration;
import com.awake.module.GameModule;
import com.awake.net2.NetContext;
import com.awake.net2.server.tcp.TcpClient;
import com.awake.packet.tcp.TcpHelloRequest;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @Author：lqh
 * @Date：2024/3/27 17:31
 */
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource(locations = {"classpath:application.properties"})
public class TcpClientTest {

    private static final Logger logger = LoggerFactory.getLogger(TcpClientTest.class);

    @Test
    public void startClient() {

        var client = new TcpClient(HostAndPort.valueOf("127.0.0.1:8088"));
        var session = client.start();

        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(2000);
            NetContext.getRouter().send(session, GameModule.TcpHelloRequest, TcpHelloRequest.valueOf("Hello, this is the tcp client!"));
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}

