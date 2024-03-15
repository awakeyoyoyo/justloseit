package com.awake.net2.protocol.definition;

import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/3/15 14:11
 */
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
