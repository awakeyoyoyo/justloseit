package com.hello.gamemodule.mission.missiongroup;

import com.hello.gamemodule.mission.entity.MissionEntity;
import com.hello.gamemodule.mission.missiongroup.handler.CommonMissionGroupHandler;

/**
 * 任务组枚举
 * @Author：lqh
 * @Date：2024/5/30 15:00
 */
public enum MissionGroupEnum implements IMissionGroupHandler {
    /**
     * 日常任务
     */
    DAILY_TASK(1, CommonMissionGroupHandler.getIns());

    private int missionGroupId;

    private IMissionGroupHandler missionGroupHandler;

    MissionGroupEnum(int missionGroupId, IMissionGroupHandler missionGroupHandler) {
        this.missionGroupId = missionGroupId;
        this.missionGroupHandler = missionGroupHandler;
    }

    public int getMissionGroupId() {
        return missionGroupId;
    }

    public IMissionGroupHandler getMissionGroupHandler() {
        return missionGroupHandler;
    }

    public static MissionGroupEnum getMissionGroupEnum(int missionGroupId) {
        for (MissionGroupEnum typeEnum : values()) {
            if (typeEnum.getMissionGroupId() == missionGroupId) {
                return typeEnum;
            }
        }
        return null;
    }

    @Override
    public void initMissionGroup(long roleId, int groupId, MissionEntity missionEntity) {
        missionGroupHandler.initMissionGroup(roleId, groupId, missionEntity);
    }
}
