package com.hello.gamemodule.condition;

import com.hello.resource.model.Condition;

/**
 * @Author：lqh
 * @Date：2024/6/3 16:32
 */
public interface IConditionHandler {

    /**
     * 校验条件
     * @param roleId
     * @return
     */
    boolean verify(long roleId, Condition condition);

}
