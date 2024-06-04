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

    @Resource
    private MissionService missionService;

    @EventReceiver
    public void handlerFunctionOpen(FunctionOpenEvent event){
        missionService.autoInitMissionGroup(event.roleId,event.functionId);
    }

}
