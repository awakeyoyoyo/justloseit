package com.hello.common;

/**
 * 定义号模块号 以及其协议号
 *
 * @Author：lqh
 * @Date：2024/3/29 9:55
 */
public interface GameProtoId {

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
