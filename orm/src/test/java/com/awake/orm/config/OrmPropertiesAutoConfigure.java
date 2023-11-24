package com.awake.orm.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: OrmPropertiesAutoConfigure
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/24 14:36
 **/

@Configuration
@EnableConfigurationProperties({OrmProperties.class})
public class OrmPropertiesAutoConfigure {
}
