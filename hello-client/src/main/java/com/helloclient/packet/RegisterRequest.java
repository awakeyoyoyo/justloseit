package com.helloclient.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author：lqh
 * @Date：2024/4/2 9:57
 */
@ProtobufClass
public class RegisterRequest {
    private String userName;
    private String password;

    public static RegisterRequest valueOf(String userName,String password) {
        RegisterRequest registerRequest=new RegisterRequest();
        registerRequest.userName=userName;
        registerRequest.password=password;
        return registerRequest;
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
