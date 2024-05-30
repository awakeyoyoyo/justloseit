package com.hello.gamemodule.mission.condition.params;

import com.hello.gamemodule.mission.condition.IConditionParams;
import lombok.Data;

/**
 * 玩家等级条件参数
 * @Author：lqh
 * @Date：2024/5/28 19:57
 */

@Data
public class RoleLevelIConditionParams implements IConditionParams {
    private int level;

    public static RoleLevelIConditionParams valueOf(long roleId, int roleLevel) {
        RoleLevelIConditionParams params=new RoleLevelIConditionParams();
        params.level=roleLevel;
        return params;
    }
}
