package com.awake.net.config.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version : 1.0
 * @ClassName: NetProperties
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/11 22:24
 **/

@Data
@ConfigurationProperties(prefix = ServerProperties.PREFIX)
public class ServerProperties {
    public static final String PREFIX = "awake.net.server";

    public static final int DEFAULT_PORT = 12400;

    private String thread;

    private String address;



}
