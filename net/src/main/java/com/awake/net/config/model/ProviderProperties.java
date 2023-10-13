package com.awake.net.config.model;

import com.awake.net.util.NetUtils;
import com.awake.util.base.StringUtils;
import com.awake.util.net.HostAndPort;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @version : 1.0
 * @ClassName: InstanceConfig
 * @Description: 服务提供者配置
 * @Auther: awake
 * @Date: 2023/8/1 10:39
 **/
@Data
@ConfigurationProperties(prefix = ProviderProperties.PREFIX)
public class ProviderProperties {
    public static final String PREFIX = "awake.net.provider";
    /** 提供服务*/

    public static final int DEFAULT_PORT = 12400;

    private String thread;

    /**
     * 地址
     */
    private String address;

    /**
     * 提供的模块
     */
    private List<ProviderModule> providers;

    public static ProviderProperties valueOf(String address, List<ProviderModule> modules) {
        ProviderProperties config = new ProviderProperties();
        config.address = address;
        config.providers = modules;
        return config;
    }

    public HostAndPort localHostAndPortOrDefault() {
        if (StringUtils.isBlank(address)) {
            var defaultHostAndPort = HostAndPort.valueOf(NetUtils.getLocalhostStr(), NetUtils.getAvailablePort(ProviderProperties.DEFAULT_PORT));
            this.address = defaultHostAndPort.toHostAndPortStr();
            return defaultHostAndPort;
        }
        return HostAndPort.valueOf(address);
    }

    public HostAndPort localHostAndPort() {
        return HostAndPort.valueOf(address);
    }
}
