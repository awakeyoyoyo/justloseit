package com.awake.orm.cache;

import com.awake.orm.model.IEntity;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author awakeyoyoyo
 */
public interface IEntityCache<PK extends Comparable<PK>, E extends IEntity<PK>> {

    /**
     * 从数据库中加载数据到缓存，如果数据库不存在则返回一个id为空的默认值，并且将这个默认值加入缓存
     */
    E load(PK pk);

    /**
     * 更新缓存中的数据，只更新缓存的时间戳，并通过一定策略写入到数据库
     */
    void update(E entity);

    /**
     * 不会删除数据库中的数据，只会删除缓存数据
     *
     * @param pk 组要删除的主键
     */
    void invalidate(PK pk);

    /**
     * 持久化所有缓存数据
     */
    void persistAll();

    /**
     * 获取所有存在的缓存对象
     */
    List<E> allPresentCaches();

    void forEach(BiConsumer<PK, E> biConsumer);

    long size();

    /**
     * 统计缓存命中率
     */
    String recordStatus();
}
