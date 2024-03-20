package com.awake.net2.packet.common;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author：lqh
 * @Date：2024/3/15 10:44
 */
@ProtobufClass
public class Heartbeat {
    @Ignore
    public static final int PROTOCOL_ID = 102;
}
