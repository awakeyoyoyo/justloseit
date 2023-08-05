package com.awake.protocol.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version : 1.0
 * @ClassName: ProtocolProperties
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/4 15:50
 **/

@Data
@ConfigurationProperties(prefix = ProtocolProperties.PREFIX)
public class ProtocolProperties {
    public static final String PREFIX = "awake.net.protocol";

    private String scanProtocolPacket;


}
