package com.awake.net.config.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @version : 1.0
 * @ClassName: RegistryConfig
 * @Description: zookeeper注册中心配置
 * @Auther: awake
 * @Date: 2023/7/12 15:21
 **/
@Data
@ConfigurationProperties(prefix = ZookeeperRegistryProperties.PREFIX)
public class ZookeeperRegistryProperties {
    public static final String PREFIX = "awake.net.zookeeper";
//
//    private String center;
//    private String user;
//    private String password;

    private int retryCount;
    private int elapsedTimeMs;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private List<String> address;

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
