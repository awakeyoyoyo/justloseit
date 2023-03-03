package com.awake.event;

import com.awakeyo.event.model.IEvent;

/**
 * @version : 1.0
 * @ClassName: LoginEvent
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/3/3 16:49
 **/
public class LoginEvent implements IEvent{

    private long roleId;

    private long loginTime;

    public static LoginEvent valueOf(int roleId, long currentTimeMillis) {
        LoginEvent event=new LoginEvent();
        event.roleId=roleId;
        event.loginTime=currentTimeMillis;
        return event;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
