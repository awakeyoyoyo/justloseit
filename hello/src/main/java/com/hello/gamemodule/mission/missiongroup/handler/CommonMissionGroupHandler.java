package com.hello.gamemodule.mission.missiongroup.handler;

import com.hello.GameContext;
import com.hello.gamemodule.condition.ConditionTypeEnum;
import com.hello.gamemodule.mission.MissionManager;
import com.hello.gamemodule.mission.MissionService;
import com.hello.gamemodule.mission.entity.MissionEntity;
import com.hello.gamemodule.mission.missiongroup.IMissionGroupHandler;
import com.hello.gamemodule.mission.struct.Mission;
import com.hello.resource.MissionResource;
import com.hello.resource.model.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用任务组初始化器 默认全部梭哈
 * @Author：lqh
 * @Date：2024/5/30 15:04
 */
public class CommonMissionGroupHandler implements IMissionGroupHandler {

    private static final CommonMissionGroupHandler ins = new CommonMissionGroupHandler();

    private CommonMissionGroupHandler() {}

    public static CommonMissionGroupHandler getIns() {
        return ins;
    }

    @Override
    public List<Mission> initMissionGroup(long roleId, int groupId, MissionEntity missionEntity) {
        MissionManager missionManager = GameContext.getIns().getComponet(MissionManager.class);
        MissionService missionService = GameContext.getIns().getComponet(MissionService.class);
        List<Integer> missionIdList = missionManager.getMissionsByGroupId(groupId);

        List<Mission> missions=new ArrayList<>();
        for (Integer missionId : missionIdList) {
            MissionResource missionResource = missionManager.getMissionResource(missionId);
            if (!verityCanAccept(roleId, missionResource)){
                continue;
            }
            Mission mission = missionService.initMission(roleId, missionId);
            missionEntity.addMission(groupId, missionResource.getProgressConditionType(), mission);
            missions.add(mission);
        }
        return missions;
    }

    @Override
    public boolean autoInit() {
        return true;
    }

}
