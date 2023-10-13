package com.awake.zookeeper;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ZookeeperRegistryProperties
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/13 15:13
 **/
@Data
public class ZookeeperRegistryProperties {

    private int retryCount;

    private int elapsedTimeMs;

    private int sessionTimeoutMs;

    private int connectionTimeoutMs;

    private String ConnectionAddress;
}
