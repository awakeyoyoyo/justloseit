package com.hello.gamemodule.mission.condition;

import com.hello.gamemodule.mission.condition.handler.RoleLevelIProgressConditionHandler;
import com.hello.gamemodule.mission.struct.Mission;

/**
 * 进度条件枚举
 * @Author：lqh
 * @Date：2024/5/28 19:51
 */
@SuppressWarnings("rawtypes")
public enum ProgressConditionTypeEnum implements IProgressConditionHandler{

    /**
     * 玩家等级进度条件
     */
    RoleLevel(1, RoleLevelIProgressConditionHandler.getIns()),

    ;
    /**
     * 进度条件类型
     */
    private int progressConditionType;

    /**
     * 进度条件处理器
     */
    @SuppressWarnings("rawtypes")
    private IProgressConditionHandler IProgressConditionHandler;

    public int getProgressConditionType() {
        return progressConditionType;
    }

    ProgressConditionTypeEnum(int progressConditionType, IProgressConditionHandler IProgressConditionHandler) {
        this.progressConditionType = progressConditionType;
        this.IProgressConditionHandler = IProgressConditionHandler;
    }

    public static ProgressConditionTypeEnum getConditionTypeEnum(int progressConditionType){
        for (ProgressConditionTypeEnum typeEnum : values()) {
            if (typeEnum.getProgressConditionType()==progressConditionType){
                return typeEnum;
            }
        }
        return null;
    }

    @Override
    public void initProgress(long roleId, Mission mission) {
        IProgressConditionHandler.initProgress(roleId, mission);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateProgress(long roleId, Mission mission, IConditionParams IConditionParams) {
        IProgressConditionHandler.updateProgress(roleId, mission, IConditionParams);
    }

    @Override
    public IConditionParams valueOfPrams(Object... objects) {
        return IProgressConditionHandler.valueOfPrams(objects);
    }
}
