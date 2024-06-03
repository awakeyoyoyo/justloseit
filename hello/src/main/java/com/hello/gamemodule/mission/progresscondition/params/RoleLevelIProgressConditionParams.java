package com.hello.gamemodule.mission.progresscondition.params;

import com.hello.gamemodule.mission.progresscondition.IProgressConditionParams;
import lombok.Data;

/**
 * 玩家等级条件参数
 * @Author：lqh
 * @Date：2024/5/28 19:57
 */

@Data
public class RoleLevelIProgressConditionParams implements IProgressConditionParams {
    private int level;

    public static RoleLevelIProgressConditionParams valueOf(long roleId, int roleLevel) {
        RoleLevelIProgressConditionParams params=new RoleLevelIProgressConditionParams();
        params.level=roleLevel;
        return params;
    }
}
