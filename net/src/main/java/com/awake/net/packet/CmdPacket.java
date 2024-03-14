package com.awake.net.packet;


import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

@ProtobufClass
public class CmdPacket {

    private int protoId;

    private byte[] packetData;

    private int signalId;

    private byte[] signalData;
}
