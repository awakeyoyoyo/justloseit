package com.awake.gateway;

import com.awake.gateway.configuration.GatewayConfiguration;
import com.awake.net.gateway.core.GatewayServer;
import com.awake.net.util.SessionUtils;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @version : 1.0
 * @ClassName: GatewayTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/3 15:16
 **/
@SpringBootTest(classes = {GatewayConfiguration.class})
@TestPropertySource(locations = {"classpath:application-gateway.properties"})
public class GatewayTest {

    /**
     * 这是网关
     */
    @Test
    public void startGateway() {
        SessionUtils.printSessionInfo();

        // 注意：这里创建的是GatewayServer里面是GatewayRouteHandler(而不是BaseRouteHandler),里面会通过ConsumerSession把消息转发到Provider
        var gatewayServer = new GatewayServer(HostAndPort.valueOf("127.0.0.1:9000"), null);
        gatewayServer.start();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
