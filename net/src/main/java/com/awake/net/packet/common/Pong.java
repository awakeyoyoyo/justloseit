package com.awake.net.packet.common;

import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: Pong
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 18:12
 **/
@Packet(protocolId = Error.PROTOCOL_ID)
@ProtobufClass
@Data
public class Pong {

    @Ignore
    public static final short PROTOCOL_ID = 104;

    /**
     * 服务器当前的时间戳
     */
    private long time;

    public static Pong valueOf(long time) {
        var pong = new Pong();
        pong.time = time;
        return pong;
    }
}
