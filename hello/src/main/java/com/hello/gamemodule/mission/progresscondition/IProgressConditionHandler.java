package com.hello.gamemodule.mission.progresscondition;

import com.hello.gamemodule.mission.struct.Mission;

/**
 * 条件进度处理器
 * @Author：lqh
 * @Date：2024/5/28 19:51
 */
public interface IProgressConditionHandler<T extends IProgressConditionParams> {

    /**
     * 初始化进度
     * @param roleId
     * @param mission
     */
    void initProgress(long roleId, Mission mission);

    /**
     * 更新进度
     * @param roleId
     * @param mission
     * @param conditionParams
     */
    void updateProgress(long roleId, Mission mission, T conditionParams);

    /**
     * 更新进度参数构建
     * @param objects
     * @return
     */
    T valueOfPrams(Object... objects);
}
