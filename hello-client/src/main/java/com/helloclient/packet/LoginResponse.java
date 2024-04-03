package com.helloclient.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author：lqh
 * @Date：2024/4/2 9:58
 */
@ProtobufClass
public class LoginResponse {
    private long rid;
    private String userName;
    private String password;

    public static Object valueOf(long rid, String userName, String password) {
        LoginResponse loginResponse=new LoginResponse();
        loginResponse.rid=rid;
        loginResponse.userName=userName;
        loginResponse.password=password;
        return loginResponse;
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

    @Override
    public String toString() {
        return "LoginResponse{" +
                "rid=" + rid +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
