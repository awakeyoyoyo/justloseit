package com.awake.zookeeper;

import com.awake.net.config.model.NetConfig;
import com.awake.net.config.model.ZookeeperRegistryProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;


/**
 * @version : 1.0
 * @ClassName: zookeeperTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/11 18:16
 **/
public class CuratorTest {

    private final ZookeeperRegistryProperties zookeeperRegistryProperties=new ZookeeperRegistryProperties();

    @Test
    public void testCurator() throws IOException {
        zookeeperRegistryProperties.setRetryCount(5);
        zookeeperRegistryProperties.setSessionTimeoutMs(60000);
        zookeeperRegistryProperties.setElapsedTimeMs(5000);
        zookeeperRegistryProperties.setConnectionTimeoutMs(5000);
        zookeeperRegistryProperties.setConnectionAddress("127.0.0.1:2181");

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                zookeeperRegistryProperties.getConnectionAddress(),
                zookeeperRegistryProperties.getSessionTimeoutMs(),
                zookeeperRegistryProperties.getConnectionTimeoutMs(),
                new RetryNTimes(zookeeperRegistryProperties.getRetryCount(), zookeeperRegistryProperties.getElapsedTimeMs())
        );
        curatorFramework.start();
        System.out.println(curatorFramework.getState());
        System.in.read();
    }

    @Test
    public void testCreateNode() throws Exception {
        zookeeperRegistryProperties.setRetryCount(5);
        zookeeperRegistryProperties.setSessionTimeoutMs(60000);
        zookeeperRegistryProperties.setElapsedTimeMs(5000);
        zookeeperRegistryProperties.setConnectionTimeoutMs(5000);
        zookeeperRegistryProperties.setConnectionAddress("127.0.0.1:2181");

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                zookeeperRegistryProperties.getConnectionAddress(),
                zookeeperRegistryProperties.getSessionTimeoutMs(),
                zookeeperRegistryProperties.getConnectionTimeoutMs(),
                new RetryNTimes(zookeeperRegistryProperties.getRetryCount(), zookeeperRegistryProperties.getElapsedTimeMs())
        );
        curatorFramework.start();
        //添加持久节点
        String path=curatorFramework.create().forPath("/curator-node");
        //添加临时序号节点
        String path2=curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/curator-node","just lose it".getBytes());

        System.out.println(String.format("curator create node :%s successful.",path));

        System.out.println(String.format("curator create ephemeral node :%s successful.",path2));

        System.in.read();
    }

}
