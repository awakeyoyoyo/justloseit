package com.awake.orm.manager;

import com.awake.orm.cache.IEntityCache;
import com.awake.orm.model.IEntity;

import java.util.Collection;

/**
 * @author awakeyoyoyo
 */
public interface IOrmManager {

    void initBefore();

    void inject();

    void initAfter();

    /**
     * EN:Get Mongo Client, through which you can get other databases or do some other complex operations
     * CN:获取MongoClient，通过MongoClient可以获取到其它数据库或者做一些其它的复杂操作
     */
//    MongoClient mongoClient();

    <E extends IEntity<?>> IEntityCache<?, E> getEntityCaches(Class<E> clazz);

    Collection<IEntityCache<?, ?>> getAllEntityCaches();

    /**
     * 基于对象的orm操作
     */
//    <E extends IEntity<?>> MongoCollection<E> getCollection(Class<E> entityClazz);

    /**
     * 更加细粒度的操作
     */
//    MongoCollection<Document> getCollection(String collection);
}
