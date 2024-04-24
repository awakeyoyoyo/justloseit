package com.hello;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 根据proto文件构建出 J proto class
 * @Author：lqh
 * @Date：2024/4/24 15:36
 */
public class ProtoGeneral {

    public static void main(String[] args) throws IOException {
        InputStream fis =new FileInputStream("F:\\justloseit\\hello\\src\\main\\java\\org\\hello\\one_message.proto");
        ProtobufIDLProxy.generateSource(fis, new File("F:\\test"));
    }
}
