package com.awake.net.packet.common;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: Ping
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 18:12
 **/
@ProtobufClass
@Data
public class Ping {

    @Ignore
    public static final int PROTOCOL_ID = 103;
}
