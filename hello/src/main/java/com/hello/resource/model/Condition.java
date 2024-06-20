package com.hello.resource.model;

import com.hello.gamemodule.condition.ConditionTypeEnum;
import com.hello.gamemodule.condition.IConditionHandler;
import lombok.Getter;

/**
 * @Author：lqh
 * @Date：2024/6/3 20:10
 */
@Getter
public class Condition  {

    /**
     * 条件类型
     */
    private int conditionType;

    /**
     * 条件参数
     */
    private String params;


    public boolean verify(long roleId) {
        ConditionTypeEnum conditionTypeEnum = ConditionTypeEnum.getConditionTypeEnum(getConditionType());
        return conditionTypeEnum.verify(roleId, this);
    }
}
