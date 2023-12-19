package com.awake.net.router.receiver;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ProtocolDefinition
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/19 14:49
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
