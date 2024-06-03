package com.hello.resource;

import com.awake.storage.anno.Id;
import com.awake.storage.anno.Storage;
import com.hello.resource.model.Condition;
import lombok.Data;

import java.util.List;

/**
 * 任务组条件配置
 * @Author：lqh
 * @Date：2024/5/28 20:33
 */
@Storage
@Data
public class MissionGroupResource {

    /**
     * 配置id
     */
    @Id
    private int confId;

    /**
     * 功能Id
     */
    private int functionId;

    /**
     * 任务组激活条件
     */
    private List<Condition> conditions;

}
