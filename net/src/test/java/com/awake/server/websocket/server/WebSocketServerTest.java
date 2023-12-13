package com.awake.server.websocket.server;

import com.awake.net.server.websocket.WebsocketServer;
import com.awake.server.ApplicationConfiguration;
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
@SpringBootTest(classes = {ApplicationConfiguration.class})
@TestPropertySource(locations = {"classpath:application-provider.properties"})
public class WebSocketServerTest {

    @Test
    public void startServer() {

        var server = new WebsocketServer(HostAndPort.valueOf("0.0.0.0:9000"));
        server.start();

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

}

