package com.awake.orm.autoconfigure;

import com.awake.orm.config.OrmProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: OrmAutoConfiguration
 * @Description: 自动装载
 * @Auther: awake
 * @Date: 2023/11/13 11:49
 **/
@Configuration
@EnableConfigurationProperties({OrmProperties.class})
public class OrmAutoConfiguration {
}
