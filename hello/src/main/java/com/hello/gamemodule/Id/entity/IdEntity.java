package com.hello.gamemodule.Id.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.model.IEntity;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:46
 */
@EntityCache
public class IdEntity implements IEntity<Integer> {

    /**
     * 模块号
     */
    @Id
    private int id;

    /**
     * 初始值
     */
    private long value;

    public static IdEntity valueOf(int id, long value) {
        IdEntity entity=new IdEntity();
        entity.id=id;
        entity.value=value;
        return entity;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
