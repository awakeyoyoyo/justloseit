package com.awake.zookeeper;

import com.awake.net.config.model.ZookeeperRegistryProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import java.io.IOException;


/**
 * @version : 1.0
 * @ClassName: zookeeperTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/11 18:16
 **/
public class CuratorTest {

    private final ZookeeperRegistryProperties zookeeperRegistryProperties = new ZookeeperRegistryProperties();

    private CuratorFramework initCuratorFramework(String s) {
        zookeeperRegistryProperties.setRetryCount(5);
        zookeeperRegistryProperties.setSessionTimeoutMs(60000);
        zookeeperRegistryProperties.setElapsedTimeMs(5000);
        zookeeperRegistryProperties.setConnectionTimeoutMs(5000);
        zookeeperRegistryProperties.setConnectionAddress(s);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                zookeeperRegistryProperties.getConnectionAddress(),
                zookeeperRegistryProperties.getSessionTimeoutMs(),
                zookeeperRegistryProperties.getConnectionTimeoutMs(),
                new RetryNTimes(zookeeperRegistryProperties.getRetryCount(), zookeeperRegistryProperties.getElapsedTimeMs())
        );
        curatorFramework.start();
        return curatorFramework;
    }

    @Test
    public void testCurator() throws IOException {
        CuratorFramework curatorFramework = initCuratorFramework("127.0.0.1:2181");
        System.out.println(curatorFramework.getState());
        System.in.read();
    }

    @Test
    public void testCreateNode() throws Exception {
        CuratorFramework curatorFramework = initCuratorFramework("127.0.0.1:2181");
        //添加持久节点
        String path = curatorFramework.create().forPath("/curator-node");
        //添加临时序号节点
        String path2 = curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/curator-node", "just lose it".getBytes());

        System.out.println(String.format("curator create node :%s successful.", path));

        System.out.println(String.format("curator create ephemeral node :%s successful.", path2));

        System.in.read();
    }

    @Test
    public void testGetData() throws Exception {
        CuratorFramework curatorFramework = initCuratorFramework("127.0.0.1:2181");

        byte[] bytes = curatorFramework.getData().forPath("/curator-node");
        System.out.println("curator-node message:" + new String(bytes));
    }


    @Test
    public void testSetData() throws Exception {
        CuratorFramework curatorFramework = initCuratorFramework("127.0.0.1:2181");

        curatorFramework.setData().forPath("/curator-node", "just lose it".getBytes());
        byte[] bytes = curatorFramework.getData().forPath("/curator-node");
        System.out.println("curator-node message:" + new String(bytes));
    }

    @Test
    public void testCreateWithParent() throws Exception {
        CuratorFramework curatorFramework = initCuratorFramework("127.0.0.1:2181");

        String pathWithParent = "/node-parent/sub-node-1";
        String path = curatorFramework.create().creatingParentsIfNeeded().forPath(pathWithParent);
        System.out.println(String.format("curator create node :%s successfully", path));
    }

    @Test
    public void testDelete() throws Exception {
        CuratorFramework curatorFramework = initCuratorFramework("127.0.0.1:2181");

        String pathWithParent = "/node-parent";

        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathWithParent);
    }

    @Test
    public void testAddNodeListener() throws Exception {
        CuratorFramework curatorFramework = initCuratorFramework("127.0.0.1:2181");
    }

}
