package com.awake.orm.cache.persister;

import com.awake.orm.OrmContext;
import com.awake.orm.cache.EntityCache;
import com.awake.orm.cache.PersisterBus;
import com.awake.orm.model.EntityDef;
import com.awake.scheduler.manager.SchedulerBus;
import com.awake.util.ExceptionUtils;
import com.awake.util.time.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronExpression;

import java.util.concurrent.TimeUnit;

/**
 * @version : 1.0
 * @ClassName: CronOrmPersister
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:08
 **/
public class CronOrmPersister extends AbstractOrmPersister {

    private static final Logger logger = LoggerFactory.getLogger(CronOrmPersister.class);

    /**
     * 持久化默认的延迟时间
     */
    private static final long DEFAULT_DELAY = 30 * TimeUtils.MILLIS_PER_SECOND;

    /**
     * cron表达式
     */
    private final CronExpression cronExpression;

    public CronOrmPersister(EntityDef entityDef, EntityCache<?, ?> entityCaches) {
        super(entityDef, entityCaches);
        this.cronExpression = CronExpression.parse(entityDef.getPersisterStrategy().getConfig());
    }

    @Override
    public void start() {
        schedulePersist();
    }

    private void schedulePersist() {
        var delay = 0L;
        try {
            var now = TimeUtils.now();
            var nextTimestamp = TimeUtils.nextTimestampByCronExpression(cronExpression, now);
            delay = nextTimestamp - now;

            if (delay < 0) {
                delay = DEFAULT_DELAY;
                logger.error("计算[cron:{}]表达式发生错误，当前时间[now:{}]，计算出来的时间[nextTimestamp:{}]，两者之差小于0"
                        , cronExpression.toString(), now, nextTimestamp);
            }
        } catch (Exception e) {
            delay = DEFAULT_DELAY;
            logger.error(ExceptionUtils.getMessage(e));
        }

        if (!OrmContext.isStop()) {
            SchedulerBus.schedule(() -> {
                if (!OrmContext.isStop()) {
                    PersisterBus.execute(() -> {
                        entityCaches.persistAll();
                        schedulePersist();
                    });
                }
            }, delay, TimeUnit.MILLISECONDS);
        }
    }
}
