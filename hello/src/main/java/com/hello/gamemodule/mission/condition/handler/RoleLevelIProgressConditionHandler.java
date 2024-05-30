package com.hello.gamemodule.mission.condition.handler;

import com.hello.gamemodule.mission.condition.IProgressConditionHandler;
import com.hello.gamemodule.mission.condition.params.RoleLevelIConditionParams;
import com.hello.gamemodule.mission.struct.Mission;

/**
 * 玩家等级条件处理器
 *
 * @Author：lqh
 * @Date：2024/5/28 20:00
 */
public class RoleLevelIProgressConditionHandler implements IProgressConditionHandler<RoleLevelIConditionParams> {
    private static final RoleLevelIProgressConditionHandler ins = new RoleLevelIProgressConditionHandler();

    private RoleLevelIProgressConditionHandler() {}

    public static RoleLevelIProgressConditionHandler getIns() {
        return ins;
    }

    @Override
    public void updateProgress(long roleId, Mission mission, RoleLevelIConditionParams conditionParams) {
        int level = conditionParams.getLevel();
        mission.setProgress(level);
    }

    @Override
    public RoleLevelIConditionParams valueOfPrams(Object... objects) {
        long roleId = (long) objects[0];
        int roleLevel = (int) objects[1];
        return RoleLevelIConditionParams.valueOf(roleId,roleLevel);
    }

    @Override
    public void initProgress(long roleId, Mission mission) {
        int level = 1;
        mission.setProgress(level);
    }


}
