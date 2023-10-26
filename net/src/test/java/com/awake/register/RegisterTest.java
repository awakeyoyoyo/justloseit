package com.awake.register;

import com.awake.NetContext;
import com.awake.net.consumer.registry.IRegistry;
import com.awake.server.ApplicationConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: RegisterTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/25 18:10
 **/
@SpringBootTest(classes = {ApplicationConfiguration.class})
public class RegisterTest {
    private static final Logger logger = LoggerFactory.getLogger(RegisterTest.class);
    @Autowired
    private NetContext netContext;
    /**
     * RPC教程：
     * 1.首先必须保证启动zookeeper
     * 2.启动服务提供者，startProvider0，startProvider1，startProvider2
     * 3.启动服务消费者，startSyncRandomConsumer，startAsyncRandomConsumer，startConsistentSessionConsumer
     * 4.每个消费者都是通过不同的策略消费，注意区别
     */

    @Test
    public void startRegister() {
        IRegistry registry = NetContext.getConfigManager().getRegistry();
        System.out.println(registry);
    }
}
