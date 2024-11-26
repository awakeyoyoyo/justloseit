package com.hello.gamemodule.mission;

import com.awake.event.anno.EventReceiver;
import com.hello.gamemodule.dailyreset.DailyResetService;
import com.hello.gamemodule.function.FunctionOpenEvent;
import com.hello.gamemodule.mission.missiongroup.MissionGroupEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author：lqh
 * @Date：2024/5/28 20:35
 */
@Component
public class MissionEventListener {

    private static final Logger logger = LoggerFactory.getLogger(MissionEventListener.class);
    private final MissionService missionService;

    public MissionEventListener(MissionService missionService) {
        this.missionService = missionService;
    }

    @EventReceiver
    public void onFunctionOpenEvent(FunctionOpenEvent event){
        MissionGroupEnum missionGroupEnum = MissionGroupEnum.getMissionGroupEnum(event.functionId);
        if (missionGroupEnum == null) {
            return;
        }
        if (missionGroupEnum.autoInit()) {
            missionService.initMissionGroup(event.roleId, event.functionId);
        }
    }

}
