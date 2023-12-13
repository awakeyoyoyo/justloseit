package com.awake.server.websocket.client;

import com.awake.NetContext;
import com.awake.net.server.websocket.WebsocketClient;
import com.awake.server.ApplicationConfiguration;
import com.awake.server.websocket.packet.WebsocketHelloRequest;
import com.awake.server.websocket.packet.WebsocketHelloResponse;
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
@SpringBootTest(classes = {ApplicationConfiguration.class})
@TestPropertySource(locations = {"classpath:application-consumer.properties"})
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
            NetContext.getRouter().send(session, request);


            ThreadUtils.sleep(1000);
            var response = NetContext.getRouter().syncAsk(session, request, WebsocketHelloResponse.class, null).packet();
            logger.info("sync client receive [packet:{}] from server", JsonUtils.object2String(response));


            NetContext.getRouter().asyncAsk(session, request, WebsocketHelloResponse.class, null)
                    .whenComplete(new Consumer<WebsocketHelloResponse>() {
                        @Override
                        public void accept(WebsocketHelloResponse jsonHelloResponse) {
                            logger.info("async client receive [packet:{}] from server", JsonUtils.object2String(jsonHelloResponse));
                        }
                    });
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
