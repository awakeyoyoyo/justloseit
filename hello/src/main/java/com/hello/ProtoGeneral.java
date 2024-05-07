package com.hello;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.hello.gamemodule.role.packet.LoginRequest;
import java.io.IOException;



/**
 *
 * 根据proto文件构建出 J proto class
 * @Author：lqh
 * @Date：2024/4/24 15:36
 */
public class ProtoGeneral {

    public static void main(String[] args) throws IOException {
        String code = ProtobufIDLGenerator.getIDL(LoginRequest.class);
        System.out.println(code);
    }
}
