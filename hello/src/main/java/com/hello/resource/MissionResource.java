package com.hello.resource;

import com.awake.storage.anno.Id;
import com.awake.storage.anno.Storage;
import com.hello.resource.model.Condition;
import com.hello.resource.model.Reward;
import lombok.Data;

import java.util.List;

/**
 * @Author：lqh
 * @Date：2024/5/28 20:02
 */
@Storage
@Data
public class MissionResource {

    /**
     * 配置id
     */
    @Id
    private int confId;

    /**
     * 任务组id
     */
    private int groupId;

    /**
     * 任务类型
     */
    private int missionType;

    /**
     * 任务进度条件
     */
    private int progressConditionType;

    /**
     * 任务进度参数
     */
    private String progressConditionParams;

    /**
     * 目标进度
     */
    private int targetProgress;

    /**
     * 奖励
     */
    private List<Reward> rewards;

    /**
     * 接取任务条件
     */
    private Condition acceptCondition;

    /**
     * 下一场任务id
     */
    private int nextMissionId;
}
