package com.awake.orm.cache.persister;

import com.awake.orm.OrmContext;
import com.awake.orm.cache.EntityCache;
import com.awake.orm.cache.PersisterBus;
import com.awake.orm.model.EntityDef;
import com.awake.scheduler.manager.SchedulerBus;
import com.awake.util.base.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @version : 1.0
 * @ClassName: TimeOrmPersister
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:08
 **/
public class TimeOrmPersister extends AbstractOrmPersister {

    /**
     * 执行的频率
     */
    private final long rate;

    public TimeOrmPersister(EntityDef entityDef, EntityCache<?, ?> entityCaches) {
        super(entityDef, entityCaches);
        this.rate = Long.parseLong(entityDef.getPersisterStrategy().getConfig());
        if (this.rate <= 0) {
            throw new RuntimeException(StringUtils.format("刷新频率[{}]不能小于0", rate));
        }
    }

    @Override
    public void start() {
        SchedulerBus.scheduleAtFixedRate(() -> {
            if (!OrmContext.isStop()) {
                PersisterBus.execute(() -> entityCaches.persistAll());
            }
        }, rate, TimeUnit.MILLISECONDS);
    }
}
