package com.awake.zookeeper;

import com.awake.net.config.model.NetConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



/**
 * @version : 1.0
 * @ClassName: zookeeperTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/11 18:16
 **/
public class zookeeperTest {
    @Test
    public void testCurator(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ZookeeperTestConfiguration.class);
        NetConfig bean = context.getBean(NetConfig.class);
        System.out.println(bean.getZookeeperConfig());
    }
}
