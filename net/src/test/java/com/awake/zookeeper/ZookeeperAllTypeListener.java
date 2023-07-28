package com.awake.zookeeper;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

/**
 * @version : 1.0
 * @ClassName: ZookeeperAllTypeListener
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/29 4:18
 **/
public class ZookeeperAllTypeListener implements CuratorCacheListener {
    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        System.out.println("event type : " + type + " :oldData: " + new String(oldData == null ? "null".getBytes() : oldData.getData()) + " :data: " + new String(data.getData()));
    }
}
