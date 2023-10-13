package com.awake.zookeeper.zookeeperlock;

import com.awake.zookeeper.ZookeeperRegistryProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.RetryNTimes;
import org.junit.Test;

/**
 * @version : 1.0
 * @ClassName: ZookeeperLockTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/29 23:09
 **/
public class ZookeeperLockTest {

    private final ZookeeperRegistryProperties zookeeperRegistryProperties = new ZookeeperRegistryProperties();

    private static final String CONNECT_STRING = "127.0.0.1:2181";

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
    public void testGetReadLock() throws Exception {
        CuratorFramework curatorFramework = initCuratorFramework(CONNECT_STRING);
        //读写锁
        InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(curatorFramework, "/lock1");
        //获取读锁对象
        InterProcessLock readLock = readWriteLock.readLock();
        System.out.println("wait to get readLock！");
        //获取所
        readLock.acquire();
        System.out.println("get readLock successfully！");
        for (int i = 0; i < 100; i++) {
            Thread.sleep(3000);
            System.out.println(String.format("keep readLock,time:%d", i));
        }
        //释放锁
        System.out.println("wait to release readLock！");
        readLock.release();
        System.out.println("release readLock successfully!");
    }

    @Test
    public void testGetWriteLock() throws Exception {
        CuratorFramework curatorFramework = initCuratorFramework(CONNECT_STRING);
        //读写锁
        InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(curatorFramework, "/lock1");
        //获取读锁对象
        InterProcessLock writeLock = readWriteLock.writeLock();
        System.out.println("wait to get writeLock！");
        writeLock.acquire();
        for (int i = 0; i < 100; i++) {
            Thread.sleep(3000);
            System.out.println(String.format("keep writeLock,time:%d", i));
        }
        //释放锁
        System.out.println("wait to release writeLock！");
        writeLock.release();
        System.out.println("release writeLock successfully!");
    }

}
