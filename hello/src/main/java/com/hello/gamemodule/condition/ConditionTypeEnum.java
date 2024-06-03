package com.hello.gamemodule.condition;

import com.hello.resource.model.ConditionConf;

/**
 * 条件类型枚举
 * @Author：lqh
 * @Date：2024/6/3 16:53
 */
public enum ConditionTypeEnum implements IConditionHandler {

    ;
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

    @Override
    public boolean verify(long roleId, ConditionConf conditionConf) {
        return conditionHandler.verify(roleId, conditionConf);
    }
}
