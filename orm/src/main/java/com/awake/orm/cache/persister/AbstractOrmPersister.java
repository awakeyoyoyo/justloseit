package com.awake.orm.cache.persister;

import com.awake.orm.cache.EntityCache;
import com.awake.orm.model.EntityDef;

/**
 * @version : 1.0
 * @ClassName: AbstractOrmPersister
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:09
 **/
public abstract class AbstractOrmPersister implements IOrmPersister {

    protected EntityDef entityDef;

    protected EntityCache<?, ?> entityCaches;



    public AbstractOrmPersister(EntityDef entityDef, EntityCache<?, ?> entityCaches) {
        this.entityDef = entityDef;
        this.entityCaches = entityCaches;
    }

}

