package com.hello.gamemodule.condition;

import com.hello.gamemodule.condition.handler.RoleLvConditionHandler;
import com.hello.gamemodule.mission.missiongroup.MissionGroupEnum;
import com.hello.resource.model.Condition;

/**
 * 条件类型枚举
 * @Author：lqh
 * @Date：2024/6/3 16:53
 */
public enum ConditionTypeEnum{
    /**
     * 玩家等级条件
     */
    ROLE_LV(1, RoleLvConditionHandler.getIns());

    /**
     * 进度条件类型
     */
    private int conditionType;

    /**
     * 进度条件处理器
     */
    @SuppressWarnings("rawtypes")
    private IConditionHandler conditionHandler;

    ConditionTypeEnum(int conditionType, IConditionHandler conditionHandler) {
        this.conditionType = conditionType;
        this.conditionHandler = conditionHandler;
    }

    public int getConditionType() {
        return conditionType;
    }

    public IConditionHandler getConditionHandler() {
        return conditionHandler;
    }


    public boolean verify(long roleId, Condition condition) {
        return conditionHandler.verify(roleId, condition);
    }

    public static ConditionTypeEnum getConditionTypeEnum(int conditionType) {
        for (ConditionTypeEnum typeEnum : values()) {
            if (typeEnum.getConditionType() == conditionType) {
                return typeEnum;
            }
        }
        return null;
    }
}
