package com.awake.cache;

import com.awake.model.IEntity;

import java.util.List;
import java.util.function.BiConsumer;

public class EntityCache implements IEntityCache{
    @Override
    public IEntity load(Comparable comparable) {
        return null;
    }

    @Override
    public void update(IEntity entity) {

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
