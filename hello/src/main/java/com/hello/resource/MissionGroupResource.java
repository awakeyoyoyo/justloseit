package com.hello.resource;

import com.awake.storage.anno.Id;
import com.awake.storage.anno.Storage;
import com.hello.resource.model.Condition;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 任务组条件配置
 * @Author：lqh
 * @Date：2024/5/28 20:33
 */
@Storage
@Getter
public class MissionGroupResource {

    /**
     * 任务组id
     */
    @Id
    private int groupId;

    /**
     * 功能Id
     */
    private int functionId;

    /**
     * 任务组激活条件
     */
    private List<Condition> conditions;

}
