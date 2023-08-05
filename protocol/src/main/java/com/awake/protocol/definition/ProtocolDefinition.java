package com.awake.protocol.definition;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ProtocolDefinition
 * @Description: proto传输对象信息类
 * @Auther: awake
 * @Date: 2023/8/4 10:24
 **/
@Data
public class ProtocolDefinition<T> {

    private int protocolId;

    private byte module;

    private Class protocolClass;
}
