package com.awake.protocol;

import com.awake.server.tcp.packet.tcp.TcpHelloRequest;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import java.io.IOException;

/**
 * @version : 1.0
 * @ClassName: ProtocolEncoTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/8 18:12
 **/
public class ProtocolEncodeTest {

    public static void main(String[] args) throws IOException {
        TcpHelloRequest tcpHelloRequest = TcpHelloRequest.valueOf("Hello, this is the tcp client!");
        Codec packetCodec =ProtobufProxy.create(tcpHelloRequest.getClass());
        byte[] packetBytes = packetCodec.encode(tcpHelloRequest);
        System.out.println("packetBytes size:"+packetBytes.length);
    }
}
