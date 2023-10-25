package com.awake.register;

import com.awake.NetContext;
import com.awake.net.util.SessionUtils;
import com.awake.register.packet.ProviderMessAnswer;
import com.awake.register.packet.ProviderMessAsk;
import com.awake.util.JsonUtils;
import com.awake.util.base.ThreadUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version : 1.0
 * @ClassName: RegisterTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/25 18:10
 **/
public class RegisterTest {
    private static final Logger logger = LoggerFactory.getLogger(RegisterTest.class);
    /**
     * RPC教程：
     * 1.首先必须保证启动zookeeper
     * 2.启动服务提供者，startProvider0，startProvider1，startProvider2
     * 3.启动服务消费者，startSyncRandomConsumer，startAsyncRandomConsumer，startConsistentSessionConsumer
     * 4.每个消费者都是通过不同的策略消费，注意区别
     */

    @Test
    public void startProvider0() {

        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    @Test
    public void startProvider1() {

        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    @Test
    public void startProvider2() {

        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

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
