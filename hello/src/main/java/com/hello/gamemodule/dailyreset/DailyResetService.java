package com.hello.gamemodule.dailyreset;

import com.awake.event.manger.EventBus;
import com.awake.net2.router.TaskBus;
import com.awake.net2.session.Session;
import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.cache.EntityCache;
import com.awake.scheduler.anno.Scheduler;
import com.hello.gamemodule.dailyreset.entity.DailyRoleDataEntity;
import com.hello.gamemodule.dailyreset.event.DailyResetEvent;
import com.hello.gamemodule.dailyreset.event.OneMinEvent;
import com.hello.gamemodule.mission.entity.MissionEntity;
import com.hello.gamemodule.role.RoleManager;
import com.hello.gamemodule.role.RoleService;
import com.hello.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：lqh
 * @Date：2024/6/18 14:10
 */
@Component
public class DailyResetService {
    private static final Logger logger = LoggerFactory.getLogger(DailyResetService.class);

    private final RoleManager roleManager;
    private final RoleService roleService;
    /**
     * 每日0点执行
     */
    @Scheduler(cron = "0 0 0 * * ?")
    public void cronDailyReset() {
        logger.info("DailyResetService dailyReset execute");
        EventBus.publicEvent(DailyResetEvent.valueOf());

        List<Session> onlineRole = roleManager.getOnlineRole();
        for (Session session : onlineRole) {
            TaskBus.execute((int) session.getUserId(), () -> roleService.doRoleDailyReset(session));
        }

    }

    /**
     * 每分钟执行
     */
    @Scheduler(cron = "0 */1 * * * ?")
    public void cronOneMin(){
        logger.info("DailyResetService oneMin execute");
        EventBus.publicEvent(OneMinEvent.valueOf(DateUtil.getHour(), DateUtil.getMin()));
    }
    public DailyResetService(RoleManager roleManager,RoleService roleService) {
        this.roleManager = roleManager;
        this.roleService=roleService;
    }

    @EntityCacheAutowired
    private EntityCache<Long, DailyRoleDataEntity> dataEntityEntityCache;

    /**
     * 更新每日值
     * @param roleId
     * @param id
     * @param value
     */
    public void saveDailyValue(long roleId, int id, long value) {
        DailyRoleDataEntity dailyRoleDataEntity = dataEntityEntityCache.loadOrCreate(roleId);
        dailyRoleDataEntity.getId2ValueMap().put(id, value);
        dataEntityEntityCache.update(dailyRoleDataEntity);
    }

    /**
     * 获取每日值
     * @param roleId
     * @param id
     * @return
     */
    public long getDailyValue(long roleId, int id) {
        DailyRoleDataEntity dailyRoleDataEntity = dataEntityEntityCache.loadOrCreate(roleId);
        return dailyRoleDataEntity.getId2ValueMap().getOrDefault(id, 0L);
    }

    /**
     * 重置每日数据
     * @param roleId
     */
    public void resetDailyValue(long roleId) {
        DailyRoleDataEntity dailyRoleDataEntity = dataEntityEntityCache.load(roleId);
        if (dailyRoleDataEntity==null){
            return;
        }
        dailyRoleDataEntity.getId2ValueMap().clear();
    }


}
