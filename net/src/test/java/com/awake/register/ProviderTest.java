package com.awake.register;

import com.awake.net.util.SessionUtils;
import com.awake.server.ApplicationConfiguration;
import com.awake.util.base.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: ProviderTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/28 21:13
 **/
@SpringBootTest(classes = {ApplicationConfiguration.class})
public class ProviderTest {
    private static final Logger logger = LoggerFactory.getLogger(ProviderTest.class);
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
}
