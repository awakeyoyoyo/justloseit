package com.hello.gamemodule.mission.missiontype;

import com.hello.gamemodule.mission.progresscondition.IProgressConditionParams;
import com.hello.gamemodule.mission.struct.Mission;
import com.hello.resource.MissionResource;
import com.hello.resource.model.Reward;

import java.util.List;

/**
 * 任务类型 处理器
 *
 * @Author：lqh
 * @Date：2024/5/30 15:39
 */
public interface IMissionTypeHandler {

    /**
     * 初始化任务
     */
    Mission initMission(long roleId, MissionResource missionResource);

    /**
     * 更新-任务
     */
    void updateMission(long roleId, Mission mission, MissionResource missionResource, IProgressConditionParams iProgressConditionParams);

    /**
     * 完成任务
     */
    List<Reward> completeMission(long roleId, Mission mission, MissionResource missionResource);

    /**
     * 能否完成任务
     */
    boolean canCompleteMission(long roleId, Mission mission, MissionResource missionResource);

    /**
     * 触发下一个任务
     */
    boolean isTriggerNextMission(MissionResource missionResource);

    boolean isCompleteDeleteMission();

}
