package com.awake.jproto;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author：lqh
 * @Date：2024/3/29 11:29
 */
public class ProtoGeneral {
    public static void main(String[] args) throws IOException {
        InputStream fis =new FileInputStream("F:\\justloseit\\hello\\src\\main\\java\\org\\hello\\one_message.proto");
        ProtobufIDLProxy.generateSource(fis, new File("F:\\test"));
    }
}
