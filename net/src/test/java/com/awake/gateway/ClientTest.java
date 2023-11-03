package com.awake.gateway;

import com.awake.NetContext;
import com.awake.gateway.configuration.GatewayConfiguration;
import com.awake.gateway.packet.GatewayToProviderRequest;
import com.awake.gateway.packet.GatewayToProviderResponse;
import com.awake.net.server.tcp.TcpClient;
import com.awake.net.util.SessionUtils;
import com.awake.util.JsonUtils;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version : 1.0
 * @ClassName: ClientTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/3 16:02
 **/

@SpringBootTest(classes = {GatewayConfiguration.class})
@TestPropertySource(locations = {"classpath:application-gateway-client.properties"})
public class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);
    /**
     * 这里是客户端，客户端先请求数据到到网关(毕竟自己连接的就是网关)
     */
    @Test
    public void clientSyncTest() {
        SessionUtils.printSessionInfo();

        // 链接网关
        var client = new TcpClient(HostAndPort.valueOf("127.0.0.1:9000"));
        var session = client.start();

        var executorSize = Runtime.getRuntime().availableProcessors() * 2;
        var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        var request = new GatewayToProviderRequest();
        request.setMessage("Hello, this is the client!");
        var atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < executorSize; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    try {
                        // 注意：这里的第2个请求参数是 xxxRequest，不是xxxAsk。  因为这里是网关要将数据转发给Provider的，因此当然不能是xxxAsk这种请求。
                        // 第3个参数argument是null，这样子随机一个服务提供者进行消息处理
                        var response = NetContext.getRouter().syncAsk(session, request, GatewayToProviderResponse.class, null).packet();
                        logger.info("客户端请求[{}]收到消息[{}]", atomicInteger.incrementAndGet(), JsonUtils.object2String(response));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            executor.execute(thread);
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    @Test
    public void clientAsyncTest() {
        SessionUtils.printSessionInfo();
        // 这里的地址是网关的地址
        var client = new TcpClient(HostAndPort.valueOf("127.0.0.1:9000"));
        var session = client.start();

        var executorSize = Runtime.getRuntime().availableProcessors() * 2;
        var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        var request = new GatewayToProviderRequest();
        request.setMessage("Hello, this is the client!");
        var atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < executorSize; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    try {
                        // 注意：这里的第2个请求参数是 xxxRequest，不是xxxAsk。  因为这里是网关要将数据转发给Provider的，因此当然不能是xxxAsk这种请求。
                        // 第3个参数argument是null，这样子随机一个服务提供者进行消息处理
                        NetContext.getRouter().asyncAsk(session, request, GatewayToProviderResponse.class, null)
                                .whenComplete(response -> {
                                    logger.info("客户端请求[{}]收到消息[{}]", atomicInteger.incrementAndGet(), JsonUtils.object2String(response));
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            executor.execute(thread);
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
