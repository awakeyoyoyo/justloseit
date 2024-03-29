package org.hello.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author：lqh
 * @Date：2024/3/29 10:42
 */
@ProtobufClass
public class HelloRequest {

    private int roleId;

    private String name;
}
