package com.awake.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version : 1.0
 * @ClassName: StorageProperties
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 17:35
 **/
@Data
@ConfigurationProperties(prefix = StorageProperties.PREFIX)
public class StorageProperties {

    public static final String PREFIX = "awake.storage";

    private String scanPackage;

    private String resourceLocation;

    /**
     * 类的属性是否可写，如果为false则类的属性必须为private并且不能有set方法
     */
    private boolean writeable;

    /**
     *   未被使用的Storage是否回收，默认开启节省资源
     */
    private boolean recycle;
}
