package com.awakeyo.net.config;

import com.awakeyo.net.config.model.ZookeeperConfig;
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
    private ZookeeperConfig zookeeperConfig;


}
