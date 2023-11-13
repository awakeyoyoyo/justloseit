package com.awake.orm.query;

import com.awake.orm.model.IEntity;

public interface IQuery {

    <E extends IEntity<?>> IQueryBuilder<E> builder(Class<E> entityClazz);
}
