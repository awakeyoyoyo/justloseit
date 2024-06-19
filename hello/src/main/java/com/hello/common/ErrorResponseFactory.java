package com.hello.common;

import com.hello.packet.RpcMsg;

import java.util.List;

/**
 * @Author：lqh
 * @Date：2024/5/17 15:08
 */
public class ErrorResponseFactory {

    public static RpcMsg.ErrorResponse create(int errorCode){
        return RpcMsg.ErrorResponse.newBuilder().setErrorCode(errorCode).build();
    }

    public static RpcMsg.ErrorResponse create(int errorCode,String... params){
        return RpcMsg.ErrorResponse.newBuilder().setErrorCode(errorCode).addAllParams(List.of(params)).build();
    }
}
