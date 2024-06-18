package com.hello.gamemodule.mission;

import com.awake.event.anno.EventReceiver;
import com.hello.gamemodule.function.FunctionOpenEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author：lqh
 * @Date：2024/5/28 20:35
 */
@Component
public class MissionEventListener {


    private final MissionService missionService;

    public MissionEventListener(MissionService missionService) {
        this.missionService = missionService;
    }

    @EventReceiver
    public void onFunctionOpenEvent(FunctionOpenEvent event){
        missionService.autoInitMissionGroup(event.roleId,event.functionId);
    }

}
