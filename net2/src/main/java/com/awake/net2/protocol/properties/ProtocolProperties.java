package com.awake.net2.protocol.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @Author：lqh
 * @Date：2024/3/15 14:11
 */
@Data
@ConfigurationProperties(prefix = ProtocolProperties.PREFIX)
public class ProtocolProperties {
    public static final String PREFIX = "awake.net.protocol";

    private String scanProtocolPacket;

    private String scanModulePacket;
}

