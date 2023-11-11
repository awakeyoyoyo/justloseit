package com.awake.manager;

import com.awake.cache.IEntityCache;
import com.awake.model.IEntity;

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
