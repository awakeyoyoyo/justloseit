package com.hello.gamemodule.mission.struct;

import lombok.Data;

/**
 * 任务
 * @Author：lqh
 * @Date：2024/5/28 19:40
 */
@Data
public class Mission {

    /**
     * 任务配置id
     */
    private int confId;

    /**
     * 分组id
     */
    private int groupId;

    /**
     * 进度
     */
    private long progress;

    /**
     * 状态
     */
    private int status;


    public static Mission valueOf(int confId, int groupId,int status) {
        Mission mission=new Mission();
        mission.confId=confId;
        mission.groupId=groupId;
        mission.status=status;
        return mission;
    }
}
