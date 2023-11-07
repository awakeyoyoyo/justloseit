package com.awake.scheduler;

import com.awake.util.base.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: SchedulerTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/7 17:48
 **/

@SpringBootTest(classes = {ApplicationConfiguration.class})
public class SchedulerTest {

    @Test
    public void startScheduler() {
        ThreadUtils.sleep(100000);
    }
}
