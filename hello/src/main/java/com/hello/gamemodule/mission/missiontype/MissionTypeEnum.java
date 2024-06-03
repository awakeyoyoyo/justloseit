package com.hello.gamemodule.mission.missiontype;

import com.hello.gamemodule.mission.progresscondition.IProgressConditionParams;
import com.hello.gamemodule.mission.struct.Mission;
import com.hello.resource.MissionResource;

/**
 * 任务类型
 *
 * @Author：lqh
 * @Date：2024/5/30 15:39
 */
public enum MissionTypeEnum implements IMissionTypeHandler {
    ;
    private int missionTypeId;

    private IMissionTypeHandler missionTypeHandler;

    MissionTypeEnum(int missionTypeId, IMissionTypeHandler missionTypeHandler) {
        this.missionTypeId = missionTypeId;
        this.missionTypeHandler = missionTypeHandler;
    }

    public int getMissionTypeId() {
        return missionTypeId;
    }

    public IMissionTypeHandler getMissionTypeHandler() {
        return missionTypeHandler;
    }

    public static MissionTypeEnum getMissionGroupEnum(int missionTypeId) {
        for (MissionTypeEnum typeEnum : values()) {
            if (typeEnum.getMissionTypeId() == missionTypeId) {
                return typeEnum;
            }
        }
        return null;
    }

    @Override
    public Mission initMission(long roleId, MissionResource missionResource) {
        return missionTypeHandler.initMission(roleId, missionResource);
    }

    @Override
    public void updateMission(long roleId, Mission mission, MissionResource missionResource, IProgressConditionParams iProgressConditionParams) {
        missionTypeHandler.updateMission(roleId, mission, missionResource, iProgressConditionParams);
    }

    @Override
    public void completeMission(long roleId, Mission mission, MissionResource missionResource) {
        missionTypeHandler.completeMission(roleId, mission, missionResource);
    }

    @Override
    public boolean canCompleteMission(long roleId, Mission mission, MissionResource missionResource) {
        return missionTypeHandler.canCompleteMission(roleId, mission, missionResource);
    }

    @Override
    public boolean isTriggerNextMission(MissionResource missionResource) {
        return missionTypeHandler.isTriggerNextMission(missionResource);
    }
}
