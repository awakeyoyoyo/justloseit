package com.awake.orm.util;

import com.awake.orm.autoconfigure.OrmAutoTestConfiguration;
import com.awake.util.base.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: MongoUuidUtilsTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 16:48
 **/
@SpringBootTest(classes = {OrmAutoTestConfiguration.class})
public class MongoUuidUtilsTest {

    @Test
    public void startApplication0() {
        incrementMongoIdTest();
    }

    @Test
    public void startApplication1() {
        incrementMongoIdTest();
    }

    @Test
    public void startApplication2() {
        incrementMongoIdTest();
    }

    @Test
    public void startApplication3() {
        incrementMongoIdTest();
    }

    private void incrementMongoIdTest() {
        ThreadUtils.sleep(8000);

        var count = 0L;
//        for (int i = 0; i < 100_0000; i++) {
//            count = MongoIdUtils.getIncrementIdFromMongoDefault("myDocument");
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                var count = 0L;
                for (int i = 0; i < 100_0000; i++) {
                    count = MongoIdUtils.getIncrementIdFromMongoDefault("myDocument");
                }
                System.out.println(count);
            }
        }).start();
        System.out.println(count);

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    @Test
    public void reset() {

        var id = MongoIdUtils.getIncrementIdFromMongoDefault("myDocument");
        System.out.println(id);
        MongoIdUtils.resetIncrementIdFromMongoDefault("myDocument");
    }

    @Test
    public void set() {
        MongoIdUtils.setIncrementIdFromMongo("myDocument", 100);
    }
}
