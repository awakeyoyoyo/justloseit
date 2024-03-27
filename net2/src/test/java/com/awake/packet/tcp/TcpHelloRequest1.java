package com.awake.packet.tcp;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @version : 1.0
 * @ClassName: TcpHelloRequest1
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/6 14:26
 **/
@ProtobufClass
public class TcpHelloRequest1  {
    @Ignore
    public static final int PROTOCOL_ID = 3000;

    private String message;

    public static TcpHelloRequest1 valueOf(String message) {
        var request = new TcpHelloRequest1();
        request.message = message;
        return request;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


