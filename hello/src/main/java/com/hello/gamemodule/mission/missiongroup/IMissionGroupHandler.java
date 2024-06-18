package com.hello.gamemodule.mission.missiongroup;

import com.hello.gamemodule.condition.ConditionTypeEnum;
import com.hello.gamemodule.mission.struct.Mission;
import com.hello.resource.MissionResource;
import com.hello.resource.model.Condition;

import java.util.List;

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
     * @param missionGroupId
     */
    List<Mission> initMissionGroup(long roleId, int missionGroupId);

    /**
     * 随着功能开发自动初始化
     * @return
     */
    boolean autoInit();

    /**
     * 清除任务组
     * @param roleId
     * @param groupId
     */
    void clearMissionGroup(long roleId,int groupId);

    /**
     * 获取任务组任务
     * @param roleId
     * @param groupId
     * @return
     */
    List<Mission> getAllMission(long roleId, int groupId);

}
