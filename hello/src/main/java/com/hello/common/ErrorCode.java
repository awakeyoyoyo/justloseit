package com.hello.common;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:26
 */
public interface ErrorCode {

    /**
     * 参数错误
     */
    int ERROR_PARAMS = 1;

    /**
     * 用户名已存在
     */
    int USER_NAME_EXIT = 2;

    /**
     * 用户名敏感
     */
    int USER_NAME_ILLEGAL = 3;
}
