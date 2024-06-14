package com.hello.gamemodule.bag.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.model.IEntity;
import com.hello.gamemodule.bag.struct.BagData;
import lombok.Data;

import java.util.Map;

/**
 * @Author：lqh
 * @Date：2024/6/4 16:42
 */
@EntityCache
@Data
public class BagEntity implements IEntity<Long> {

    @Id
    private long id;


    /**
     * 背包类型 ->背包数据
     */
    private Map<String, BagData> bagDataMap;

    @Override
    public Long id() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
