package com.awake.orm.autoconfigure;

import com.awake.orm.OrmContext;
import com.awake.orm.accessor.MongodbAccessor;
import com.awake.orm.cache.PersisterBus;
import com.awake.orm.config.OrmProperties;
import com.awake.orm.manager.OrmManager;
import com.awake.orm.query.MongodbQuery;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

    @Bean
    @ConditionalOnMissingBean
    public PersisterBus persisterBus() {
        return new PersisterBus();
    }

    @Bean
    public OrmManager ormManager() {
        return new OrmManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public MongodbAccessor mongodbAccessor() {
        return new MongodbAccessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MongodbQuery mongodbQuery() {
        return new MongodbQuery();
    }


    @Bean
    @ConditionalOnMissingBean
    public OrmContext ormContext() {
        return new OrmContext();
    }

}
