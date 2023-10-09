package com.awake.server.server;

import com.awake.net.server.tcp.TcpServer;
import com.awake.server.ApplicationConfiguration;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: TcpServerTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:22
 **/
@SpringBootTest(classes = {ApplicationConfiguration.class})
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
