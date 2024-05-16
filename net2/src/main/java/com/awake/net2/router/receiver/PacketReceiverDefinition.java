package com.awake.net2.router.receiver;

import com.awake.net2.session.Session;
import com.awake.util.ReflectionUtils;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @version : 1.0
 * @ClassName: PacketReceiverDefinition
 * @Description: 动态代理被PacketReceiver注解标注的方法，为了避免反射最终会用javassist字节码增强的方法去代理PacketReceiverDefinition
 * @Auther: awake
 * @Date: 2023/9/6 11:37
 **/

@Data
public class PacketReceiverDefinition implements IPacketReceiver {
    /**
     * A controller bean
     */
    private Object bean;

    /**
     * Methods annotated by PacketReceiver annotations, eg: public void atTcpHelloRequest(Session session, TcpHelloRequest request)
     */
    private Method method;

    /**
     * The protocol class that receives the package, eg: TcpHelloRequest
     */
    private Class<?> packetClazz;

    /**
     * The protocol class parser date method
     */
    private Method protobufParser;

    public PacketReceiverDefinition(Object bean, Method method, Class<?> packetClazz) {
        this.bean = bean;
        this.method = method;
        this.packetClazz = packetClazz;
        ReflectionUtils.makeAccessible(method);
    }

    @Override
    public void invoke(Session session, Object packet) {

        ReflectionUtils.invokeMethod(bean, method, session, packet);

    }


}
