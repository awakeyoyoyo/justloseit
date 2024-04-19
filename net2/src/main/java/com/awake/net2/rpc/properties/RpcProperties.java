package com.awake.net2.rpc.properties;

import com.awake.net2.protocol.properties.ProtocolProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author：lqh
 * @Date：2024/4/18 16:00
 */
@Data
@ConfigurationProperties(prefix = RpcProperties.PREFIX)
public class RpcProperties {
    public static final String PREFIX = "awake.net.rpc";

    private String scanRpcServerPacket;

    private String scanRpcClientPacket;

    private String rpcProviderPort;      //

    private String rpcConsumerHosts;
}
