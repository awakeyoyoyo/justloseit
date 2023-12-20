package com.awake.net.protocol.definition;

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

    private Class<T> protocolClass;

    private int module;

    public static ProtocolDefinition valueOf(int protocolId, Class clazz) {
        ProtocolDefinition bean = new ProtocolDefinition();
        bean.setProtocolId(protocolId);
        bean.setProtocolClass(clazz);
        return bean;
    }

    public static ProtocolDefinition valueOf(int protocolId, int module, Class clazz) {
        ProtocolDefinition bean = new ProtocolDefinition();
        bean.setProtocolId(protocolId);
        bean.setProtocolClass(clazz);
        bean.setModule(module);
        return bean;
    }
}
