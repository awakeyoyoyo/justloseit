package com.awake.query;

import com.awake.model.IEntity;

public interface IQuery {

    <E extends IEntity<?>> IQueryBuilder<E> builder(Class<E> entityClazz);
}
