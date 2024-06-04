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
    private long roleId;


    private Map<Integer, BagData> bagDataMap;

    @Override
    public Long id() {
        return roleId;
    }

    @Override
    public void setId(Long id) {
        this.roleId = id;
    }
}
