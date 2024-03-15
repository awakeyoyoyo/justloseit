package com.awake.net2.packet.common;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/3/15 10:44
 */
@ProtobufClass
@Data
public class Error {
    private int module;

    private int errorCode;

    private String errorMessage;

}
