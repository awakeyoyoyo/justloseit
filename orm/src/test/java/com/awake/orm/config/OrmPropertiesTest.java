package com.awake.orm.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: OrmPropertiesTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/24 14:33
 **/

@SpringBootTest(classes = {OrmPropertiesAutoConfigure.class})
public class OrmPropertiesTest {
    @Autowired
    private OrmProperties ormProperties;

    @Test
    public void testOrmProperties() {
        System.out.println(ormProperties);
    }
}
