package com.hello.gamemodule.role.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.model.IEntity;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:00
 */
@EntityCache
public class RoleEntity implements IEntity<String> {
    @Id
    private String id;
    private long rid;
    private String password;

    @Override
    public String id() {
        return id;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

