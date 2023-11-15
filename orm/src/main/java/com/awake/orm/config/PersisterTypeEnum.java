package com.awake.orm.config;

import com.awake.orm.cache.EntityCache;
import com.awake.orm.cache.persister.CronOrmPersister;
import com.awake.orm.cache.persister.IOrmPersister;
import com.awake.orm.cache.persister.TimeOrmPersister;
import com.awake.orm.model.EntityDef;
import com.awake.util.base.StringUtils;

/**
 * @version : 1.0
 * @ClassName: PersisterTypeEnum
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:07
 **/
public enum  PersisterTypeEnum {
    //
    QUEUE {
        @Override
        public IOrmPersister createPersister(EntityDef entityDef, EntityCache<?, ?> entityCaches) {
            return null;
        }
    },
    CRON {
        @Override
        public IOrmPersister createPersister(EntityDef entityDef, EntityCache<?, ?> entityCaches) {
            return new CronOrmPersister(entityDef, entityCaches);
        }
    },
    TIME {
        @Override
        public IOrmPersister createPersister(EntityDef entityDef, EntityCache<?, ?> entityCaches) {
            return new TimeOrmPersister(entityDef, entityCaches);
        }
    };


    public static PersisterTypeEnum getPersisterType(String persisterType) {
        for (PersisterTypeEnum persister : values()) {
            if (persister.name().equalsIgnoreCase(persisterType)) {
                return persister;
            }
        }
        throw new IllegalArgumentException(StringUtils.format("无效的持久化类型[persisterType:{}]", persisterType));
    }

    public abstract IOrmPersister createPersister(EntityDef entityDef, EntityCache<?, ?> entityCaches);
}
