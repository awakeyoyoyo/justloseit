package com.hello.gamemodule.condition.handler;

import com.hello.gamemodule.condition.IConditionHandler;
import com.hello.gamemodule.mission.missiongroup.handler.CommonMissionGroupHandler;
import com.hello.resource.model.Condition;

/**
 * @Author：lqh
 * @Date：2024/6/4 10:09
 */
public class RoleLvConditionHandler implements IConditionHandler {

    private static final RoleLvConditionHandler ins = new RoleLvConditionHandler();

    private RoleLvConditionHandler() {}

    public static RoleLvConditionHandler getIns() {
        return ins;
    }
    @Override
    public boolean verify(long roleId, Condition condition) {
        int level = 0;
        return level >= Integer.parseInt(condition.getParams());
    }
}
