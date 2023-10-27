package com.awake.register;

import com.awake.net.util.SessionUtils;
import com.awake.register.configuration.RegisterConfiguration;
import com.awake.util.base.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @version : 1.0
 * @ClassName: ProviderTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/28 21:13
 **/
@SpringBootTest(classes = {RegisterConfiguration.class})
@TestPropertySource(locations = {"classpath:application-provider.properties"})
public class Provider0Test {
    private static final Logger logger = LoggerFactory.getLogger(Provider0Test.class);
    @Test
    public void startProvider0() {

        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}

@SpringBootTest(classes = {RegisterConfiguration.class})
@TestPropertySource(locations = {"classpath:application-provider01.properties"})
class Provider1Test {
    private static final Logger logger = LoggerFactory.getLogger(Provider1Test.class);
    @Test
    public void startProvider1() {

        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}

@SpringBootTest(classes = {RegisterConfiguration.class})
@TestPropertySource(locations = {"classpath:application-provider02.properties"})
class Provider2Test {
    private static final Logger logger = LoggerFactory.getLogger(Provider2Test.class);
    @Test
    public void startProvider2() {

        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
