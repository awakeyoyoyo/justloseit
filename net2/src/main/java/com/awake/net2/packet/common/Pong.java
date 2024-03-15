package com.awake.net2.packet.common;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/3/15 10:43
 */
@ProtobufClass
@Data
public class Pong {

    /**
     * 服务器当前的时间戳
     */
    private long time;
}
