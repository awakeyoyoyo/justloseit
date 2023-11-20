package com.awake.orm.manager;

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
import com.awake.util.ReflectionUtils;
import com.awake.util.base.StringUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author awakeyoyoyo
 */
public class OrmManager implements IOrmManager{
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
    }

    @Override
    public void inject() {

    }

    @Override
    public void initAfter() {

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
        return null;
    }

    @Override
    public MongoCollection<Document> getCollection(String collection) {
        return null;
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
