package com.awake.orm.manager;

import com.awake.orm.cache.IEntityCache;
import com.awake.orm.model.IEntity;

import java.util.Collection;

/**
 * @author awakeyoyoyo
 */
public class OrmManager implements IOrmManager{


    @Override
    public void initBefore() {

    }

    @Override
    public void inject() {

    }

    @Override
    public void initAfter() {

    }

    @Override
    public <E extends IEntity<?>> IEntityCache<?, E> getEntityCaches(Class<E> clazz) {
        return null;
    }

    @Override
    public Collection<IEntityCache<?, ?>> getAllEntityCaches() {
        return null;
    }
}
