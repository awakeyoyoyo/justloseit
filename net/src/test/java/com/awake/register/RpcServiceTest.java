package com.awake.register;

import com.awake.NetContext;
import com.awake.net.util.SessionUtils;
import com.awake.register.packet.ProviderMessAnswer;
import com.awake.register.packet.ProviderMessAsk;
import com.awake.server.ApplicationConfiguration;
import com.awake.util.JsonUtils;
import com.awake.util.base.ThreadUtils;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version : 1.0
 * @ClassName: ConsumerTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/28 21:14
 **/

@SpringBootTest(classes = {ApplicationConfiguration.class})

public class RpcServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(RpcServiceTest.class);
    /**
     * 同步请求的方式
     */
    @Test
    public void startSyncRandomConsumer() throws Exception {

        SessionUtils.printSessionInfo();

        var ask = new ProviderMessAsk();
        ask.setMessage("Hello, this is the consumer!");
        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(1000);
            var response = NetContext.getConsumer().syncAsk(ask, ProviderMessAnswer.class, null).packet();
            logger.info("消费者请求[{}]收到消息[{}]", i, JsonUtils.object2String(response));
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    /**
     * 异步请求的方式
     */
    @Test
    public void startAsyncRandomConsumer() {

        SessionUtils.printSessionInfo();

        var ask = new ProviderMessAsk();
        ask.setMessage("Hello, this is the consumer!");
        var atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(1000);
            NetContext.getConsumer().asyncAsk(ask, ProviderMessAnswer.class, null).whenComplete(answer -> {
                logger.info("消费者请求[{}]收到消息[{}]", atomicInteger.incrementAndGet(), JsonUtils.object2String(answer));
            });
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
