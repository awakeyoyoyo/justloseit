package com.awake.accessor;

import com.awake.model.IEntity;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 对数据库进行（增，删，改）的相关方法
 *
 * @author awake
 */
public interface IAccessor {
    <E extends IEntity<?>> boolean insert(E entity);

    <E extends IEntity<?>> void batchInsert(List<E> entities);

    <E extends IEntity<?>> boolean update(E entity);

    <E extends IEntity<?>> void batchUpdate(List<E> entities);

    <E extends IEntity<?>> boolean delete(E entity);

    <E extends IEntity<?>> boolean delete(Object pk, Class<E> entityClazz);

    <E extends IEntity<?>> void batchDelete(List<E> entities);

    <E extends IEntity<?>> void batchDelete(List<?> pks, Class<E> entityClazz);

    @Nullable
    <E extends IEntity<?>> E load(Object pk, Class<E> entityClazz);
}
