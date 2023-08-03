package com.awake.net.config;

import com.awake.net.config.model.NetConfig;
import com.awake.net.config.model.ZookeeperRegistryProperties;
import com.awake.net.registry.IRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version : 1.0
 * @ClassName: ConfigManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/12 15:27
 **/
public class ConfigManager implements IConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    /**
     * zookeeper配置
     */
    private ZookeeperRegistryProperties zookeeperConfig;


    @Override
    public NetConfig getLocalConfig() {
        return null;
    }

    @Override
    public void initRegistry() {

    }

    @Override
    public IRegistry getRegistry() {
        return null;
    }
}
