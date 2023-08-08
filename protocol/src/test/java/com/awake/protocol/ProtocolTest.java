package com.awake.protocol;

import com.awake.ProtocolContext;
import com.awake.protocol.definition.ProtocolDefinition;
import com.awake.protocol.packet.AAA;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @version : 1.0
 * @ClassName: ProtocolTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/4 16:13
 **/


@SpringBootTest(classes = {ApplicationConfiguration.class})
public class ProtocolTest {
    @Autowired
    private ApplicationConfiguration applicationConfiguration;
    @Autowired
    private ProtocolContext protocolContext;
    @Autowired
    private ProtocolManager protocolManager;
    @Test
    public void testProtocol() {
        System.out.println(protocolManager);
    }

    @Test
    public void testProtocolScan() {
        System.out.println(protocolManager.getProtocolDefinitionHashMap());
    }

    @Test
    public void testProtocolCodec() throws IOException {
        ProtocolDefinition definition = protocolManager.getProtocol(1);
        AAA aaa=new AAA();
        aaa.setA(9527);
        aaa.setAa("awake");
        Codec packetCodec = ProtobufProxy.create(definition.getProtocolClass());
        byte[] encode = packetCodec.encode(aaa);

        AAA decode = (AAA) packetCodec.decode(encode);
        System.out.println(decode.toString());
    }
}
