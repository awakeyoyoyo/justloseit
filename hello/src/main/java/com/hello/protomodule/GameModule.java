package com.hello.protomodule;

/**
 * @Author：lqh
 * @Date：2024/3/29 9:55
 */
public interface GameModule {

    int ModuleId = 1;

    // 协议
    /**
     * 错误码
     */
    int ErrorResponse = 10;

    /**
     * 登录
     */
    int LoginRequest = 1000;
    int LoginResponse = 1001;
    /**
     * 注册
     */
    int RegisterRequest = 1002;
    int RegisterResponse = 1003;
}
