package com.hello.gamemodule.mission.progresscondition.handler;

import com.hello.gamemodule.mission.progresscondition.IProgressConditionHandler;
import com.hello.gamemodule.mission.progresscondition.params.RoleLevelIProgressConditionParams;
import com.hello.gamemodule.mission.struct.Mission;

/**
 * 玩家等级条件处理器
 *
 * @Author：lqh
 * @Date：2024/5/28 20:00
 */
public class RoleLevelIProgressConditionHandler implements IProgressConditionHandler<RoleLevelIProgressConditionParams> {
    private static final RoleLevelIProgressConditionHandler ins = new RoleLevelIProgressConditionHandler();

    private RoleLevelIProgressConditionHandler() {}

    public static RoleLevelIProgressConditionHandler getIns() {
        return ins;
    }

    @Override
    public void updateProgress(long roleId, Mission mission, RoleLevelIProgressConditionParams conditionParams) {
        int level = conditionParams.getLevel();
        mission.setProgress(level);
    }

    @Override
    public RoleLevelIProgressConditionParams valueOfPrams(Object... objects) {
        long roleId = (long) objects[0];
        int roleLevel = (int) objects[1];
        return RoleLevelIProgressConditionParams.valueOf(roleId,roleLevel);
    }

    @Override
    public void initProgress(long roleId, Mission mission) {
        int level = 1;
        mission.setProgress(level);
    }


}
