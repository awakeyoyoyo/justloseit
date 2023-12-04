package com.awake.storage.config;

import com.awake.storage.autoconfigure.StorageAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: StorageConfigTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/4 18:07
 **/
@SpringBootTest(classes = {StorageAutoConfiguration.class})
public class StorageConfigTest {
    @Autowired
    private StorageProperties properties;
    @Test
    public void test() throws InterruptedException {
        System.out.println(properties);
    }
}
