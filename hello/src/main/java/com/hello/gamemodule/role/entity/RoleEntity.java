package com.hello.gamemodule.role.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.model.IEntity;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:00
 */
@EntityCache
public class RoleEntity implements IEntity<Long> {
    @Id
    private long id;
    private String userName;
    private String password;

    private long dailyResetTime;

    @Override
    public Long id() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public long getDailyResetTime() {
        return dailyResetTime;
    }

    public void setDailyResetTime(long dailyResetTime) {
        this.dailyResetTime = dailyResetTime;
    }
}

