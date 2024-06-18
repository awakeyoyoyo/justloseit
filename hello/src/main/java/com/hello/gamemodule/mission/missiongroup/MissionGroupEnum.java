package com.hello.gamemodule.mission.missiongroup;

import com.hello.gamemodule.mission.missiongroup.handler.CommonMissionGroupHandler;
import com.hello.gamemodule.mission.struct.Mission;

import java.util.List;

/**
 * 任务组枚举
 * @Author：lqh
 * @Date：2024/5/30 15:00
 */
public enum MissionGroupEnum {
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

    public List<Mission> initMissionGroup(long roleId) {
       return missionGroupHandler.initMissionGroup(roleId, this.missionGroupId);
    }

    public boolean autoInit() {
        return missionGroupHandler.autoInit();
    }
}
