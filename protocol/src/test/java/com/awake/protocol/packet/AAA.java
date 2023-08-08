package com.awake.protocol.packet;

import com.awake.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: AAA
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/5 18:17
 **/
@ProtobufClass
@Packet(protocolId = 1)
@Data
public class AAA {
    private int a;
    private String aa;
}
