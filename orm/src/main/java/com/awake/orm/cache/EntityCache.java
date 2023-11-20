package com.awake.orm.cache;

import com.awake.orm.OrmContext;
import com.awake.orm.cache.persister.PNode;
import com.awake.orm.model.EntityDef;
import com.awake.orm.model.IEntity;
import com.awake.util.AssertionUtils;
import com.awake.util.base.CollectionUtils;
import com.awake.util.base.ThreadUtils;
import com.awake.util.time.TimeUtils;
import com.github.benmanes.caffeine.cache.*;
import com.mongodb.client.model.Filters;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class EntityCache<PK extends Comparable<PK>, E extends IEntity<PK>> implements IEntityCache<PK, E> {
    private static final Logger logger = LoggerFactory.getLogger(EntityCache.class);

    private static final int BATCH_SIZE = 512;

    private final EntityDef entityDef;

    private final LoadingCache<PK, PNode<E>> cache;

    public EntityCache(EntityDef entityDef) {
        this.entityDef = entityDef;
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(entityDef.getExpireMillisecond(), TimeUnit.MILLISECONDS)
                .maximumSize(entityDef.getCacheSize())
                .initialCapacity(CollectionUtils.comfortableCapacity(entityDef.getCacheSize()))
                //.recordStats() // 开启统计信息开关，cache.stats()获取统计信息
                .removalListener(new RemovalListener<PK, PNode<E>>() {
                    @Override
                    public void onRemoval(@Nullable PK pk, @Nullable PNode<E> pnode, @NonNull RemovalCause removalCause) {
                        if (pnode.getWriteToDbTime() == pnode.getModifiedTime()) {
                            return;
                        }

                        // 缓存失效之前，将数据写入数据库
                        var entity = pnode.getEntity();
                        @SuppressWarnings("unchecked")
                        var entityClass = (Class<E>) entityDef.getClazz();
                        var collection = OrmContext.getOrmManager().getCollection(entityClass);

                        var version = entity.gvs();
                        entity.svs(version + 1);

                        var filter = entity.gvs() > 0
                                ? Filters.and(Filters.eq("_id", entity.id()), Filters.eq("vs", version))
                                : Filters.eq("_id", entity.id());
                        var result = collection.replaceOne(filter, entity);
                        if (result.getModifiedCount() <= 0) {
                            logger.warn("移除[removalCause:{}]缓存时，更新数据库[{}]中的实体主键[pk:{}]的文档异常"
                                    , removalCause, entityDef.getClazz().getSimpleName(), entity.id());
                        }
                    }
                })
                .build(new CacheLoader<PK, PNode<E>>() {
                    @Override
                    public @Nullable PNode<E> load(@NonNull PK pk) {
                        @SuppressWarnings("unchecked")
                        var entity = (E) OrmContext.getAccessor().load(pk, (Class<IEntity<?>>) entityDef.getClazz());

                        // 如果数据库中不存在则给一个默认值
                        if (entity == null) {
                            @SuppressWarnings("unchecked")
                            var newEntity = (E) entityDef.newEntity(pk);
                            return new PNode<E>(newEntity);
                        }

                        return new PNode<E>(entity);
                    }
                });
    }

    @Override
    public E load(PK pk) {
        AssertionUtils.notNull(pk);
        try {
            return cache.get(pk).getEntity();
        } catch (Exception e) {
            logger.error("数据库[{}]缓存[pk:{}]加载发生exception异常", entityDef.getClazz().getSimpleName(), pk, e);
        } catch (Throwable t) {
            logger.error("数据库[{}]缓存[pk:{}]加载发生error异常", entityDef.getClazz().getSimpleName(), pk, t);
        }

        logger.warn("数据库[{}]无法加载缓存[pk:{}]，返回默认值", entityDef.getClazz().getSimpleName(), pk);
        @SuppressWarnings("unchecked")
        var entity = (E) entityDef.newEntity(pk);
        var pnode = new PNode<E>(entity);
        cache.put(pk, pnode);
        return entity;
    }

    @Override
    public void update(IEntity entity) {
        AssertionUtils.notNull(entity);

        var currentPnode = cache.getIfPresent(entity.id());

        if (currentPnode == null) {
            currentPnode = new PNode<>(entity);
            cache.put(entity.id(), currentPnode);
        }

        var pnodeThreadId = currentPnode.getThreadId();
        var currentThreadId = Thread.currentThread().getId();
        if (pnodeThreadId != currentThreadId) {
            if (pnodeThreadId == 0) {
                currentPnode.setThreadId(currentThreadId);
            } else {
                var pnodeThread = ThreadUtils.findThread(pnodeThreadId);
                if (pnodeThread == null) {
                    logger.warn("[{}][id:{}]有并发写风险，第一次更新的线程[threadId:{}]，第2次更新的线程[threadId:{}]", entity.getClass().getSimpleName(), entity.id(), pnodeThreadId, currentThreadId);
                } else {
                    logger.warn("[{}][id:{}]有并发写风险，第一次更新的线程[threadId:{}][threadName:{}]，第2次更新的线程[threadId:{}][threadName:{}]"
                            , entity.getClass().getSimpleName(), entity.id(), pnodeThreadId, pnodeThread.getName(), currentThreadId, Thread.currentThread().getName());
                }
            }
        }

        // 加100以防止，立刻加载并且立刻修改数据的情况发生时，服务器取到的时间戳相同
        currentPnode.setModifiedTime(TimeUtils.now() + 100);
    }

    @Override
    public void invalidate(Comparable comparable) {

    }

    @Override
    public void persistAll() {

    }

    @Override
    public List allPresentCaches() {
        return null;
    }

    @Override
    public void forEach(BiConsumer biConsumer) {

    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public String recordStatus() {
        return null;
    }
}
