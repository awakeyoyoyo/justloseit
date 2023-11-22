package com.awake.orm.manager;

import com.awake.exception.RunException;
import com.awake.orm.OrmContext;
import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.anno.Id;
import com.awake.orm.anno.Index;
import com.awake.orm.anno.IndexText;
import com.awake.orm.cache.EntityCache;
import com.awake.orm.cache.IEntityCache;
import com.awake.orm.config.OrmProperties;
import com.awake.orm.model.EntityDef;
import com.awake.orm.model.IEntity;
import com.awake.orm.model.IndexDef;
import com.awake.orm.model.IndexTextDef;
import com.awake.util.AssertionUtils;
import com.awake.util.JsonUtils;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.CollectionUtils;
import com.awake.util.base.StringUtils;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author awakeyoyoyo
 */
public class OrmManager implements IOrmManager {
    @Autowired
    private OrmProperties ormConfig;

    private MongoClient mongoClient;

    private MongoDatabase mongodbDatabase;

    /**
     * 全部的Entity定义，key为对应的class，value为当前的Entity是否在当前项目中以缓存的形式使用
     */
    private final Map<Class<?>, Boolean> allEntityCachesUsableMap = new HashMap<>();

    private final Map<Class<? extends IEntity<?>>, IEntityCache<?, ?>> entityCachesMap = new HashMap<>();

    private final Map<Class<? extends IEntity<?>>, String> collectionNameMap = new ConcurrentHashMap<>();

    @Override
    public void initBefore() {
        var entityDefMap = entityClass();

        for (var entityDef : entityDefMap.values()) {
            @SuppressWarnings("rawtypes")
            var entityCaches = new EntityCache(entityDef);
            entityCachesMap.put(entityDef.getClazz(), entityCaches);
            allEntityCachesUsableMap.put(entityDef.getClazz(), false);
        }
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

        mongoClient = MongoClients.create(mongoBuilder.build());
        mongodbDatabase = mongoClient.getDatabase(ormConfig.getDatabase());

        // 创建索引
        for (var entityDef : entityDefMap.values()) {
            var indexDefMap = entityDef.getIndexDefMap();
            if (CollectionUtils.isNotEmpty(indexDefMap)) {
                var collection = getCollection(entityDef.getClazz());
                for (var indexDef : indexDefMap.entrySet()) {
                    var fieldName = indexDef.getKey();
                    var index = indexDef.getValue();
                    var hasIndex = false;
                    for (var document : collection.listIndexes()) {
                        var keyDocument = (Document) document.get("key");
                        if (keyDocument.containsKey(fieldName)) {
                            hasIndex = true;
                        }
                    }
                    if (!hasIndex) {
                        var indexOptions = new IndexOptions();
                        indexOptions.unique(index.isUnique());

                        if (index.getTtlExpireAfterSeconds() > 0) {
                            indexOptions.expireAfter(index.getTtlExpireAfterSeconds(), TimeUnit.SECONDS);
                        }

                        if (index.isAscending()) {
                            collection.createIndex(Indexes.ascending(fieldName), indexOptions);
                        } else {
                            collection.createIndex(Indexes.descending(fieldName), indexOptions);
                        }
                    }
                }
            }

            var indexTextDefMap = entityDef.getIndexTextDefMap();
            if (CollectionUtils.isNotEmpty(indexTextDefMap)) {
                AssertionUtils.isTrue(indexTextDefMap.size() == 1
                        , StringUtils.format("A collection can have only one text index [{}]", JsonUtils.object2String(indexTextDefMap.keySet())));
                var collection = getCollection(entityDef.getClazz());
                for (var indexTextDef : indexTextDefMap.entrySet()) {
                    var fieldName = indexTextDef.getKey();
                    var hasIndex = false;
                    for (var document : collection.listIndexes()) {
                        var keyDocument = (Document) document.get("key");
                        if (keyDocument.containsKey(fieldName)) {
                            hasIndex = true;
                        }
                    }
                    if (!hasIndex) {
                        collection.createIndex(Indexes.text(fieldName));
                    }
                }
            }
        }
    }

    @Override
    public void inject() {
        var applicationContext = OrmContext.getApplicationContext();
        var componentBeans = applicationContext.getBeansWithAnnotation(Component.class);
        for (var bean : componentBeans.values()) {
            ReflectionUtils.filterFieldsInClass(bean.getClass()
                    , field -> field.isAnnotationPresent(EntityCacheAutowired.class)
                    , field -> {
                        Type type = field.getGenericType();

                        if (!(type instanceof ParameterizedType)) {
                            // 注入的变量类型需要是泛型类
                            throw new RuntimeException(StringUtils.format("The variable [{}] is not of type generic", field.getName()));
                        }

                        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                        @SuppressWarnings("unchecked")
                        var entityClazz = (Class<? extends IEntity<?>>) types[1];
                        IEntityCache<?, ?> entityCaches = entityCachesMap.get(entityClazz);

                        if (entityCaches == null) {
                            // entity-package需要配置到可以扫描到EntityCache注解的包
                            throw new RunException("The EntityCache object does not exist, please check that [entity-package:{}] and [entityCaches:{}] are configured in the correct position", ormConfig.getEntityPackage(), entityClazz);
                        }

                        ReflectionUtils.makeAccessible(field);
                        ReflectionUtils.setField(field, bean, entityCaches);
                        allEntityCachesUsableMap.put(entityClazz, true);
                    });
        }
    }

    @Override
    public void initAfter() {
        allEntityCachesUsableMap.entrySet().stream()
                .filter(it -> !it.getValue())
                .map(it -> it.getKey())
                .forEach(it -> entityCachesMap.remove(it));
    }

    @Override
    public MongoClient mongoClient() {
        return null;
    }

    @Override
    public <E extends IEntity<?>> IEntityCache<?, E> getEntityCaches(Class<E> clazz) {
        return null;
    }

    @Override
    public Collection<IEntityCache<?, ?>> getAllEntityCaches() {
        return null;
    }

    @Override
    public <E extends IEntity<?>> MongoCollection<E> getCollection(Class<E> entityClazz) {
        var collectionName = collectionNameMap.get(entityClazz);
        if (collectionName == null) {
            collectionName = StringUtils.substringBeforeLast(StringUtils.uncapitalize(entityClazz.getSimpleName()), "Entity");
            collectionNameMap.put(entityClazz, collectionName);
        }
        return mongodbDatabase.getCollection(collectionName, entityClazz);
    }

    @Override
    public MongoCollection<Document> getCollection(String collection) {
        return mongodbDatabase.getCollection(collection);
    }


    private Map<Class<? extends IEntity<?>>, EntityDef> entityClass() {
        var classSet = new HashSet<>();

        var classes = scanEntityCacheAnno();
        classSet.addAll(classes);


        var cacheDefMap = new HashMap<Class<? extends IEntity<?>>, EntityDef>();
        for (var clazz : classSet) {
            @SuppressWarnings("unchecked")
            var entityClass = (Class<? extends IEntity<?>>) clazz;
            var cacheDef = parserEntityDef(entityClass);
            cacheDefMap.putIfAbsent(entityClass, cacheDef);
        }
        return cacheDefMap;
    }

    private Set<Class<?>> scanEntityCacheAnno() {
        var scanLocation = ormConfig.getEntityPackage();
        var prefixPattern = "classpath*:";
        var suffixPattern = "**/*.class";


        var resourcePatternResolver = new PathMatchingResourcePatternResolver();
        var metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        try {
            String packageSearchPath = prefixPattern + scanLocation.replace(StringUtils.PERIOD, StringUtils.SLASH) + StringUtils.SLASH + suffixPattern;
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            var result = new HashSet<Class<?>>();
            String name = com.awake.orm.anno.EntityCache.class.getName();
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    AnnotationMetadata annoMeta = metadataReader.getAnnotationMetadata();
                    if (annoMeta.hasAnnotation(name)) {
                        ClassMetadata clazzMeta = metadataReader.getClassMetadata();
                        result.add(Class.forName(clazzMeta.getClassName()));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("No Entity was scanned:" + e);
        }
    }

    public EntityDef parserEntityDef(Class<? extends IEntity<?>> clazz) {
        var cacheStrategies = ormConfig.getCaches();
        var persisterStrategies = ormConfig.getPersisters();

        var entityCache = clazz.getAnnotation(com.awake.orm.anno.EntityCache.class);
        var cache = entityCache.cache();
        var cacheStrategyOptional = cacheStrategies.stream().filter(it -> it.getStrategy().equals(cache.value())).findFirst();
        // Entity需要有@Cache注解的缓存策略
        AssertionUtils.isTrue(cacheStrategyOptional.isPresent(), "No Entity[{}] @Cache policy found[{}]", clazz.getSimpleName(), cache.value());

        var cacheStrategy = cacheStrategyOptional.get();
        var cacheSize = cacheStrategy.getSize();
        var expireMillisecond = cacheStrategy.getExpireMillisecond();

        var idField = ReflectionUtils.getFieldsByAnnoInPOJOClass(clazz, Id.class)[0];
        ReflectionUtils.makeAccessible(idField);

        var persister = entityCache.persister();
        var persisterStrategyOptional = persisterStrategies.stream().filter(it -> it.getStrategy().equals(persister.value())).findFirst();
        // 实体类Entity需要有持久化策略
        AssertionUtils.isTrue(persisterStrategyOptional.isPresent(), "Entity[{}] No persistence strategy found[{}]", clazz.getSimpleName(), persister.value());

        var persisterStrategy = persisterStrategyOptional.get();
        var indexDefMap = new HashMap<String, IndexDef>();
        var fields = ReflectionUtils.getFieldsByAnnoInPOJOClass(clazz, Index.class);
        for (var field : fields) {
            var indexAnnotation = field.getAnnotation(Index.class);

            if (indexAnnotation.ttlExpireAfterSeconds() > 0) {
                var fieldType = field.getGenericType();
                if (!(fieldType == Date.class || field.getGenericType().toString().equals("java.util.List<java.util.Date>"))) {
                    // MongoDB规定TTL类型必须是Date，List<Date>的其中一种类型
                    throw new IllegalArgumentException(StringUtils.format("MongoDB TTL type:[{}] must be Date or List<Date>", field.getName()));
                }
            }

            IndexDef indexDef = new IndexDef(field, indexAnnotation.ascending(), indexAnnotation.unique(), indexAnnotation.ttlExpireAfterSeconds());
            indexDefMap.put(field.getName(), indexDef);
        }

        var indexTextDefMap = new HashMap<String, IndexTextDef>();
        fields = ReflectionUtils.getFieldsByAnnoInPOJOClass(clazz, IndexText.class);
        for (var field : fields) {
            IndexTextDef indexTextDef = new IndexTextDef(field, field.getAnnotation(IndexText.class));
            indexTextDefMap.put(field.getName(), indexTextDef);
        }

        return EntityDef.valueOf(idField, clazz, cacheSize, expireMillisecond, persisterStrategy, indexDefMap, indexTextDefMap);
    }

}
