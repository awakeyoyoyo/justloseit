package com.hello.gamemodule.dailyreset.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.model.IEntity;
import lombok.Data;

import java.util.Map;

/**
 * 每日玩家数据 主要每日任务用到
 * @Author：lqh
 * @Date：2024/6/21 15:27
 */
@EntityCache
@Data
public class DailyRoleDataEntity implements IEntity<Long> {

    /**
     * 玩家id
     */
    @Id
    private long id;
    /**
     * 标识对应数据
     */
    private Map<Integer,Long> id2ValueMap;

    @Override
    public Long id() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }
}
