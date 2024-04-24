package com.awake.net2.router;

import com.awake.exception.RunException;
import com.awake.net2.NetContext;
import com.awake.net2.router.receiver.EnhanceUtils;
import com.awake.net2.router.receiver.IPacketReceiver;
import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.router.receiver.PacketReceiverDefinition;
import com.awake.net2.session.Session;
import com.awake.util.AssertionUtils;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.ArrayUtils;
import com.awake.util.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author：lqh
 * @Date：2024/3/15 17:30
 */
public class PacketBus {

    /**
     * 网络包的约定规则如下：
     * 1. 客户端的请求约定以Request结尾，服务器的响应约定以Response结尾
     * 2. 服务器内部请求约定以Ask结尾，服务器内部的响应约定以Answer结尾
     * 3. 服务器主动通知客户端以Notice结尾
     * 4. 公共的协议放在common模块
     */
    public static final String NET_REQUEST_SUFFIX = "Request";
    public static final String NET_RESPONSE_SUFFIX = "Response";

    public static final String NET_ASK_SUFFIX = "Ask";
    public static final String NET_ANSWER_SUFFIX = "Answer";

    public static final String NET_NOTICE_SUFFIX = "Notice";



    private static final Logger logger = LoggerFactory.getLogger(PacketBus.class);

    private static final Map<Integer, IPacketReceiver> receiverMap = new HashMap<>();

    public void init(ApplicationContext applicationContext) {
        // 注册协议接收器
        Map<String, Object> componentBeans = applicationContext.getBeansWithAnnotation(Component.class);
        for (Object bean : componentBeans.values()) {
            registerPacketReceiverDefinition(bean);
        }
    }


    /**
     * The routing of the message
     */
    public static void route(Session session, Object packet) {
        IPacketReceiver receiver = receiverMap.get(NetContext.getProtocolManager().getProtocolId(packet.getClass()));
        if (receiver == null) {
            String name = packet.getClass().getSimpleName();
            throw new RuntimeException(StringUtils.format("no any packetReceiver:[at{}] found for this packet:[{}] or no GatewayAttachment sent back if this server is gateway", name, name));
        }
        receiver.invoke(session, packet);
    }


    public void registerPacketReceiverDefinition(Object bean) {
        Class<?> clazz = bean.getClass();

        Method[] methods = ReflectionUtils.getMethodsByAnnoInPOJOClass(clazz, PacketReceiver.class);
        if (ArrayUtils.isEmpty(methods)) {
            return;
        }

        if (!ReflectionUtils.isPojoClass(clazz)) {
            logger.warn("The message registration class [{}] is not a POJO class, and the parent class will not be scanned", clazz);
        }

        for (Method method : methods) {
            Class<?>[] paramClazzs = method.getParameterTypes();

            AssertionUtils.isTrue(paramClazzs.length == 2 || paramClazzs.length == 3, "[class:{}] [method:{}] must have two or three parameter!", bean.getClass().getName(), method.getName());

            AssertionUtils.isTrue(Session.class.isAssignableFrom(paramClazzs[0]), "[class:{}] [method:{}],the first parameter must be Session type parameter Exception.", bean.getClass().getName(), method.getName());

            Class<?> packetClazz = paramClazzs[1];
            String packetName = packetClazz.getCanonicalName();
            String methodName = method.getName();

            AssertionUtils.isTrue(Modifier.isPublic(method.getModifiers()), "[class:{}] [method:{}] [packet:{}] must use 'public' as modifier!", bean.getClass().getName(), methodName, packetName);

            AssertionUtils.isTrue(!Modifier.isStatic(method.getModifiers()), "[class:{}] [method:{}] [packet:{}] can not use 'static' as modifier!", bean.getClass().getName(), methodName, packetName);

            String expectedMethodName = StringUtils.format("at{}", packetClazz.getSimpleName());
            AssertionUtils.isTrue(methodName.equals(expectedMethodName), "[class:{}] [method:{}] [packet:{}] expects '{}' as method name!", bean.getClass().getName(), methodName, packetName, expectedMethodName);

            int protocolId = Integer.MIN_VALUE;
            try {
                protocolId = NetContext.getProtocolManager().getProtocolId(packetClazz);
            } catch (Exception e) {
                throw new RunException("[class:{}][protocolId:{}] has no registration, please register for this protocol", packetClazz.getSimpleName(), protocolId);
            }

            try {
                AssertionUtils.isNull(receiverMap.get(protocolId), "duplicate protocol registration, @PacketReceiver [class:{}] is repeatedly received [at{}]", packetClazz.getSimpleName(), packetClazz.getSimpleName());

                PacketReceiverDefinition receiverDefinition = new PacketReceiverDefinition(bean, method, packetClazz);
                IPacketReceiver enhanceReceiverDefinition = EnhanceUtils.createPacketReceiver(receiverDefinition);
                receiverMap.put(protocolId, enhanceReceiverDefinition);
//                logger.info("Register successfully! [class:{}] [method:{}] [packet:{}]", bean.getClass().getName(), methodName, packetName);
            } catch (Throwable t) {
                throw new RunException("Registration protocol [class:{}] unknown exception", packetClazz.getSimpleName(), t);
            }
        }
    }
}
