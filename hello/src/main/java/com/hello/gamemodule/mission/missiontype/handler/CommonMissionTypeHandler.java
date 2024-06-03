package com.hello.gamemodule.mission.missiontype.handler;

import com.hello.gamemodule.mission.progresscondition.IProgressConditionParams;
import com.hello.gamemodule.mission.progresscondition.ProgressConditionTypeEnum;
import com.hello.gamemodule.mission.constant.MissionStatus;
import com.hello.gamemodule.mission.missiontype.IMissionTypeHandler;
import com.hello.gamemodule.mission.struct.Mission;
import com.hello.resource.MissionResource;

/**
 * 通用任务类型
 *
 * @Author：lqh
 * @Date：2024/5/30 16:03
 */
public class CommonMissionTypeHandler implements IMissionTypeHandler {

    @Override
    public Mission initMission(long roleId, MissionResource missionResource) {
        Mission mission = Mission.valueOf(missionResource.getConfId(),
                missionResource.getGroupId(), MissionStatus.MISSION_ACCEPT_STATUS);

        ProgressConditionTypeEnum conditionTypeEnum = ProgressConditionTypeEnum.getConditionTypeEnum(missionResource.getProgressConditionType());
        if (conditionTypeEnum == null) {
            throw new RuntimeException("mission init Mission error. roleId:" + roleId + "missionConfigId:" + missionResource.getConfId());
        }
        conditionTypeEnum.initProgress(roleId, mission);
        return mission;
    }

    @Override
    public void updateMission(long roleId, Mission mission, MissionResource missionResource, IProgressConditionParams iProgressConditionParams) {
        int progressConditionType = missionResource.getProgressConditionType();
        ProgressConditionTypeEnum conditionTypeEnum = ProgressConditionTypeEnum.getConditionTypeEnum(progressConditionType);
        if (conditionTypeEnum == null) {
            throw new RuntimeException("mission init Mission error. roleId:" + roleId + "missionConfigId:" + missionResource.getConfId());
        }
        conditionTypeEnum.updateProgress(roleId, mission, iProgressConditionParams);
        if (mission.getProgress() >= missionResource.getTargetProgress()) {
            mission.setStatus(MissionStatus.MISSION_CAN_COMPLETE_STATUS);
        }
    }

    @Override
    public void completeMission(long roleId, Mission mission, MissionResource missionResource) {

    }

    @Override
    public boolean canCompleteMission(long roleId, Mission mission, MissionResource missionResource) {
        return mission.getStatus() == MissionStatus.MISSION_CAN_COMPLETE_STATUS && mission.getProgress() >= missionResource.getTargetProgress();
    }

    @Override
    public boolean isTriggerNextMission(MissionResource missionResource) {
        return missionResource.getNextMissionId() != 0;
    }

}
