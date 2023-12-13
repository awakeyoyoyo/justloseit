package com.awake.server.tcp.client;

import com.awake.NetContext;
import com.awake.net.server.tcp.TcpClient;
import com.awake.server.ApplicationConfiguration;
import com.awake.server.tcp.packet.tcp.TcpHelloRequest;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @version : 1.0
 * @ClassName: TcpClientTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:13
 **/
@SpringBootTest(classes = {ApplicationConfiguration.class})
@TestPropertySource(locations = {"classpath:application-consumer.properties"})
public class TcpClientTest {

    private static final Logger logger = LoggerFactory.getLogger(TcpClientTest.class);

    @Test
    public void startClient() {

        var client = new TcpClient(HostAndPort.valueOf("127.0.0.1:8088"));
        var session = client.start();

        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(2000);
            NetContext.getRouter().send(session, TcpHelloRequest.valueOf("Hello, this is the tcp client!"));

//            NetContext.getRouter().send(session, TcpHelloRequest.valueOf("Hello, this is the tcp client!"), new SignalAttachment());

        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
