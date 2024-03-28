package com.awake.server.websocket.client;


import com.awake.configuration.TestConfiguration;
import com.awake.module.GameModule;
import com.awake.net2.NetContext;
import com.awake.net2.server.webscoket.WebsocketClient;
import com.awake.packet.websocket.WebsocketHelloRequest;
import com.awake.packet.websocket.WebsocketHelloResponse;
import com.awake.util.JsonUtils;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: WebSocketClientTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/13 17:42
 **/
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource(locations = {"classpath:application.properties"})
public class WebSocketClientTest {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientTest.class);

    @Test
    public void startClient() throws Exception {
        var webSocketClientProtocolConfig = WebSocketClientProtocolConfig.newBuilder()
                .webSocketUri("ws://127.0.0.1:9000/websocket")
                .build();

        var client = new WebsocketClient(HostAndPort.valueOf("127.0.0.1:9000"), webSocketClientProtocolConfig);
        var session = client.start();

        var request = new WebsocketHelloRequest();
        request.setMessage("Hello, this is the websocket client!");

        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(1000);
            NetContext.getRouter().send(session, GameModule.WebsocketHelloRequest, request);
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
