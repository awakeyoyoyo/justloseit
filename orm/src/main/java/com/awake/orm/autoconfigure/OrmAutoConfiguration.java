package com.awake.orm.autoconfigure;

import com.awake.orm.OrmContext;
import com.awake.orm.accessor.MongodbAccessor;
import com.awake.orm.cache.PersisterBus;
import com.awake.orm.config.OrmProperties;
import com.awake.orm.manager.OrmManager;
import com.awake.orm.query.MongodbQuery;
import com.awake.util.base.CollectionUtils;
import com.awake.util.base.StringUtils;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

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
    public MongoClient mongoClient(OrmProperties ormConfig) {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        var mongoBuilder = MongoClientSettings
                .builder()
                .codecRegistry(pojoCodecRegistry);

        // 设置数据库地址
        if (CollectionUtils.isNotEmpty(ormConfig.getAddress())) {
            var hostList = ormConfig.getAddress().values()
                    .stream()
                    .map(it -> it.split(StringUtils.COMMA_REGEX))
                    .flatMap(it -> Arrays.stream(it))
                    .filter(it -> StringUtils.isNotBlank(it))
                    .map(it -> StringUtils.trim(it))
                    .map(it -> it.split(StringUtils.COLON_REGEX))
                    .map(it -> new ServerAddress(it[0], Integer.parseInt(it[1])))
                    .collect(Collectors.toList());
            mongoBuilder.applyToClusterSettings(builder -> builder.hosts(hostList));
        }

        // 设置数据库账号密码
        if (StringUtils.isNotBlank(ormConfig.getUser()) && StringUtils.isNotBlank(ormConfig.getPassword())) {
            mongoBuilder.credential(MongoCredential.createCredential(ormConfig.getUser(), "admin", ormConfig.getPassword().toCharArray()));
        }

        // 设置连接池的大小
        var maxConnection = Runtime.getRuntime().availableProcessors() * 2 + 1;
        mongoBuilder.applyToConnectionPoolSettings(builder -> builder.maxSize(maxConnection).minSize(1));
        return MongoClients.create(mongoBuilder.build());
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
