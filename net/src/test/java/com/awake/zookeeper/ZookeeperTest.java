package com.awake.zookeeper;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * @version : 1.0
 * @ClassName: zookeeper
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/26 11:19
 **/
@SpringBootTest(classes = ZookeeperTest.class)
@ComponentScan("com.awake.net")
public class ZookeeperTest {

    @Autowired
    private ZookeeperRegistryProperties zookeeperRegistryProperties;

    @Test
    public void testApplicationProperties() {
        System.out.println(zookeeperRegistryProperties.toString());
    }
}
