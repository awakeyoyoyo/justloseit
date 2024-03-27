package com.awake.packet.tcp;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @version : 1.0
 * @ClassName: TcpHelloResponse
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:18
 **/

@ProtobufClass
public class TcpHelloResponse  {
    @Ignore
    public static final int PROTOCOL_ID = 1501;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
