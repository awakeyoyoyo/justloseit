package com.hello.gamemodule.mission.missiongroup;

import com.hello.gamemodule.mission.entity.MissionEntity;

/**
 * 任务组 处理器
 * @Author：lqh
 * @Date：2024/5/30 15:02
 */
public interface IMissionGroupHandler {

    /**
     * 初始化组任务的逻辑
     * 常用：所有任务都接取
     * 主线：只接取第一个 后续触发接取
     * 等等... 应对后续策划奇葩逻辑
     *
     * @param roleId
     * @param groupId
     * @param missionEntity
     */
    void initMissionGroup(long roleId, int groupId, MissionEntity missionEntity);
}
