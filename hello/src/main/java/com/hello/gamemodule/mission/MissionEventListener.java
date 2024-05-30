package com.hello.gamemodule.mission;

import com.awake.event.anno.EventReceiver;
import com.hello.gamemodule.function.FunctionOpenEvent;
import com.hello.gamemodule.mission.condition.ProgressConditionTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author：lqh
 * @Date：2024/5/28 20:35
 */
@Component
public class MissionEventListener {

    @Resource
    private MissionService missionService;

    @EventReceiver
    public void handlerFunctionOpen(FunctionOpenEvent event){
        missionService.initMissionGroup(event.roleId,event.functionId);

        missionService.updateMission(event.roleId, ProgressConditionTypeEnum.RoleLevel);
    }

}
