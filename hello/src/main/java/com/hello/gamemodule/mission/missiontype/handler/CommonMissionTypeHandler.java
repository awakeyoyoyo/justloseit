package com.hello.gamemodule.mission.missiontype.handler;

import com.hello.gamemodule.mission.MissionEventListener;
import com.hello.gamemodule.mission.missiongroup.handler.CommonMissionGroupHandler;
import com.hello.gamemodule.mission.progresscondition.IProgressConditionParams;
import com.hello.gamemodule.mission.progresscondition.ProgressConditionTypeEnum;
import com.hello.gamemodule.mission.constant.MissionStatus;
import com.hello.gamemodule.mission.missiontype.IMissionTypeHandler;
import com.hello.gamemodule.mission.struct.Mission;
import com.hello.resource.MissionResource;
import com.hello.resource.model.Reward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 通用任务类型
 *
 * @Author：lqh
 * @Date：2024/5/30 16:03
 */
public class CommonMissionTypeHandler implements IMissionTypeHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommonMissionTypeHandler.class);
    private static final CommonMissionTypeHandler ins = new CommonMissionTypeHandler();

    CommonMissionTypeHandler() {
    }

    public static CommonMissionTypeHandler getIns() {
        return ins;
    }

    @Override
    public Mission initMission(long roleId, MissionResource missionResource) {
        Mission mission = Mission.valueOf(missionResource.getConfId(),
                missionResource.getGroupId(), MissionStatus.MISSION_ACCEPT_STATUS);

        ProgressConditionTypeEnum conditionTypeEnum = ProgressConditionTypeEnum.getConditionTypeEnum(missionResource.getProgressConditionType());
        if (conditionTypeEnum == null) {
            logger.error("mission init Mission conditionTypeEnum not exit. roleId:{},missionConfigId:{}", roleId, missionResource.getConfId());
            return null;
        }
        conditionTypeEnum.initProgress(roleId, mission);
        if (mission.getProgress() >= missionResource.getTargetProgress()) {
            mission.setStatus(MissionStatus.MISSION_CAN_COMPLETE_STATUS);
        }
        return mission;
    }

    @Override
    public void updateMission(long roleId, Mission mission, MissionResource missionResource, IProgressConditionParams iProgressConditionParams) {
        int progressConditionType = missionResource.getProgressConditionType();
        ProgressConditionTypeEnum conditionTypeEnum = ProgressConditionTypeEnum.getConditionTypeEnum(progressConditionType);
        if (conditionTypeEnum == null) {
            logger.error("mission update Mission conditionTypeEnum not exit. roleId:{},missionConfigId:{}", roleId, missionResource.getConfId());
            return;
        }
        conditionTypeEnum.updateProgress(roleId, mission, iProgressConditionParams);
        if (mission.getProgress() >= missionResource.getTargetProgress()) {
            mission.setStatus(MissionStatus.MISSION_CAN_COMPLETE_STATUS);
        }
    }

    @Override
    public List<Reward> completeMission(long roleId, Mission mission, MissionResource missionResource) {
        mission.setStatus(MissionStatus.MISSION_COMPLETE_STATUS);
        //发奖励
        return missionResource.getRewards();
    }

    @Override
    public boolean canCompleteMission(long roleId, Mission mission, MissionResource missionResource) {
        return mission.getStatus() == MissionStatus.MISSION_CAN_COMPLETE_STATUS && mission.getProgress() >= missionResource.getTargetProgress();
    }

    @Override
    public boolean isTriggerNextMission(MissionResource missionResource) {
        return missionResource.getNextMissionId() != 0;
    }

    @Override
    public boolean isCompleteDeleteMission() {
        return true;
    }


}
