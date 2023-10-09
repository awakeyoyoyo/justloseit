package com.awake.net.router.receiver;

import com.awake.net.session.Session;
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
     * attachment class, eg: GatewayAttachment
     */
    private Class<?> attachmentClazz;

    public PacketReceiverDefinition(Object bean, Method method, Class<?> packetClazz, Class<?> attachmentClazz) {
        this.bean = bean;
        this.method = method;
        this.packetClazz = packetClazz;
        this.attachmentClazz = attachmentClazz;
        ReflectionUtils.makeAccessible(method);
    }

    @Override
    public void invoke(Session session, Object packet, Object attachment) {
        if (attachmentClazz == null) {
            ReflectionUtils.invokeMethod(bean, method, session, packet);
        } else {
            ReflectionUtils.invokeMethod(bean, method, session, packet, attachment);
        }
    }


}
