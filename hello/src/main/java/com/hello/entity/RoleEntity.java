package com.hello.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.model.IEntity;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:00
 */
@EntityCache
public class RoleEntity implements IEntity<String> {
    private long rid;
    @Id
    private String userName;
    private String password;

    @Override
    public String id() {
        return userName;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

