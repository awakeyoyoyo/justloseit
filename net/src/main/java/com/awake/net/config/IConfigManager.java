package com.awake.net.config;

import com.awake.net.config.model.NetConfig;
import com.awake.net.consumer.registry.IRegistry;

/**
 * @version : 1.0
 * @ClassName: IConfigManager
 * @Description: 配置管理器
 * @Auther: awake
 * @Date: 2023/7/12 15:27
 **/
public interface IConfigManager {
    NetConfig getNetConfig();

    void initRegistry();

    IRegistry getRegistry();
}
