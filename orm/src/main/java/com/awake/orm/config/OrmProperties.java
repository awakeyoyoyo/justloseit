package com.awake.orm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: OrmProperties
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:03
 **/
@Data
@ConfigurationProperties(prefix = OrmProperties.PREFIX)
public class OrmProperties {
    public static final String PREFIX = "awake.orm";

    /** database */
    private String database;

    private String user;

    private String password;

    private Map<String, String> address;

    /** caches */
    private List<CacheStrategy> caches;

    private String entityPackage;

    /** persister */
    private List<PersisterStrategy> persisters;
}
