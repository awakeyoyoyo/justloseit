package com.awake.server.tcp.server;

import com.awake.configuration.TestConfiguration;
import com.awake.net2.server.tcp.TcpServer;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @Author：lqh
 * @Date：2024/3/27 19:35
 */
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource(locations = {"classpath:application.properties"})
public class TcpServerTest {


    /**
     * 单机服务器教程，启动成功过后在com.zfoo.net.core.tcp.client.TcpClientTest中运行startClientTest
     * <p>
     * startClientTest连接服务器成功过后，会不断的发消息给服务器
     */
    @Test
    public void startServer() {
        var server = new TcpServer(HostAndPort.valueOf("127.0.0.1:8088"));
        server.start();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

}
