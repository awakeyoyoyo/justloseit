package com.awake.net.config.model;

import com.awake.util.base.StringUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: NetProperties
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/11 22:24
 **/

@Data
@ConfigurationProperties(prefix = RegistryProperties.PREFIX)
public class RegistryProperties {

    public static final String PREFIX = "awake.net.registry";

    private String center;

    private String user;

    private String password;

    private Map<String, String> address;

    public boolean hasZookeeperAuthor() {
        return !(StringUtils.isBlank(user) || StringUtils.isBlank(password));
    }

    public String toZookeeperAuthor() {
        return user + StringUtils.COLON + password;
    }


}
