package com.hello.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:33
 */
@ConfigurationProperties(prefix = GameServerProperties.PREFIX)
public class GameServerProperties {
    public static final String PREFIX = "game.server";
    /**
     * 服务器id
     */
    private int serverId;


    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
