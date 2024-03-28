package com.awake.server.websocket.server;


import com.awake.configuration.TestConfiguration;
import com.awake.net2.server.webscoket.WebsocketServer;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @version : 1.0
 * @ClassName: WebSocketServerTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/13 17:47
 **/
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource(locations = {"classpath:application.properties"})
public class WebSocketServerTest {

    @Test
    public void startServer() {

        var server = new WebsocketServer(HostAndPort.valueOf("127.0.0.1:9000"));
        server.start();

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

}

