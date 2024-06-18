package com.hello.gamemodule.dailyreset;

import com.awake.event.manger.EventBus;
import com.awake.net2.router.TaskBus;
import com.awake.net2.session.Session;
import com.awake.scheduler.SchedulerContext;
import com.awake.scheduler.anno.Scheduler;
import com.awake.scheduler.manager.SchedulerBus;
import com.hello.gamemodule.dailyreset.event.DailyResetEvent;
import com.hello.gamemodule.dailyreset.event.OneMinEvent;
import com.hello.gamemodule.dailyreset.event.RoleDailyResetEvent;
import com.hello.gamemodule.role.RoleManager;
import com.hello.gamemodule.role.RoleService;
import com.hello.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author：lqh
 * @Date：2024/6/18 14:10
 */
@Component
public class DailyResetService {


    private final RoleManager roleManager;
    private final RoleService roleService;
    public DailyResetService(RoleManager roleManager,RoleService roleService) {
        this.roleManager = roleManager;
        this.roleService=roleService;
    }

    /**
     * 每日0点执行
     */
    @Scheduler(cron = "0 0 0 * * ?")
    public void cronDayReset() {
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
        EventBus.publicEvent(OneMinEvent.valueOf(DateUtil.getHour(), DateUtil.getMin()));
    }

}
