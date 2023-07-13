package com.awakeyo.net.config.model;

/**
 * @version : 1.0
 * @ClassName: NetConfig
 * @Description: 模块核心配置
 * @Auther: awake
 * @Date: 2023/7/12 15:44
 **/
public class NetConfig {

    /**
     * zookeeper配置
     */
    private ZookeeperConfig zookeeperConfig;

    public ZookeeperConfig getZookeeperConfig() {
        return zookeeperConfig;
    }

    public void setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
        this.zookeeperConfig = zookeeperConfig;
    }
}
