package com.awake.packet.tcp;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @version : 1.0
 * @ClassName: TcpHelloRequest2
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/6 14:26
 **/
@ProtobufClass
public class TcpHelloResponse1  {
    @Ignore
    public static final int PROTOCOL_ID = 3001;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

