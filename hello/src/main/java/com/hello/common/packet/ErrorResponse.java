package com.hello.common.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:21
 */
@ProtobufClass
public class ErrorResponse {
    private int errorCode;

    private List<String> params;

    public static ErrorResponse valueOf(int errorCode) {
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.errorCode=errorCode;
        errorResponse.params=new ArrayList<>();
        return errorResponse;
    }
}
