package com.helloclient.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author：lqh
 * @Date：2024/4/2 9:55
 */
@ProtobufClass
public class LoginRequest {
    private String userName;

    private String password;

    public static LoginRequest valueOf(String userName, String password) {
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.userName=userName;
        loginRequest.password=password;
        return loginRequest;
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
