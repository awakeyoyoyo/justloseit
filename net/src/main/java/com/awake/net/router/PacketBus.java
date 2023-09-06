package com.awake.net.router;

import com.awake.ProtocolContext;
import com.awake.exception.RunException;
import com.awake.net.packet.IPacket;
import com.awake.net.router.attachment.GatewayAttachment;
import com.awake.net.router.attachment.IAttachment;
import com.awake.net.router.receiver.EnhanceUtils;
import com.awake.net.router.receiver.IPacketReceiver;
import com.awake.net.router.receiver.PacketReceiver;
import com.awake.net.router.receiver.PacketReceiverDefinition;
import com.awake.net.session.Session;
import com.awake.util.ArrayUtils;
import com.awake.util.AssertionUtils;
import com.awake.util.ReflectionUtils;
import com.awake.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: PacketBus
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/6 11:20
 **/
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


    public static final String NET_COMMON_MODULE = "common";


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
    public static void route(Session session, IPacket packet, IAttachment attachment) {
        IPacketReceiver receiver = receiverMap.get(packet.protocolId());
        if (receiver == null) {
            String name = packet.getClass().getSimpleName();
            throw new RuntimeException(StringUtils.format("no any packetReceiver:[at{}] found for this packet:[{}] or no GatewayAttachment sent back if this server is gateway", name, name));
        }
        receiver.invoke(session, packet, attachment);
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

            AssertionUtils.isTrue(IPacket.class.isAssignableFrom(paramClazzs[1]), "[class:{}] [method:{}],the second parameter must be IPacket type parameter Exception.", bean.getClass().getName(), method.getName());

            AssertionUtils.isTrue(paramClazzs.length != 3 || IAttachment.class.isAssignableFrom(paramClazzs[2]), "[class:{}] [method:{}],the third parameter must be IAttachment type parameter Exception.", bean.getClass().getName(), method.getName());

            Class<?> packetClazz = paramClazzs[1];
            Class<?> attachmentClazz = paramClazzs.length == 3 ? paramClazzs[2] : null;
            String packetName = packetClazz.getCanonicalName();
            String methodName = method.getName();

            AssertionUtils.isTrue(Modifier.isPublic(method.getModifiers()), "[class:{}] [method:{}] [packet:{}] must use 'public' as modifier!", bean.getClass().getName(), methodName, packetName);

            AssertionUtils.isTrue(!Modifier.isStatic(method.getModifiers()), "[class:{}] [method:{}] [packet:{}] can not use 'static' as modifier!", bean.getClass().getName(), methodName, packetName);

            String expectedMethodName = StringUtils.format("at{}", packetClazz.getSimpleName());
            AssertionUtils.isTrue(methodName.equals(expectedMethodName), "[class:{}] [method:{}] [packet:{}] expects '{}' as method name!", bean.getClass().getName(), methodName, packetName, expectedMethodName);

            // If the request class name ends with Request, then the attachment should be a Gateway Attachment
            // If the request class name ends with Ask, then attachment cannot be a Gateway Attachment
            if (attachmentClazz != null) {
                if (packetName.endsWith(NET_REQUEST_SUFFIX)) {
                    AssertionUtils.isTrue(attachmentClazz.equals(GatewayAttachment.class), "[class:{}] [method:{}] [packet:{}] must use [attachment:{}]!", bean.getClass().getName(), methodName, packetName, GatewayAttachment.class.getCanonicalName());
                } else if (packetName.endsWith(NET_ASK_SUFFIX)) {
                    AssertionUtils.isTrue(!attachmentClazz.equals(GatewayAttachment.class), "[class:{}] [method:{}] [packet:{}] can not match with [attachment:{}]!", bean.getClass().getName(), methodName, packetName, GatewayAttachment.class.getCanonicalName());
                }
            }

            int protocolId = Integer.MIN_VALUE;
            try {
                protocolId = ProtocolContext.getProtocolContext().getProtocolManager().getProtocolId(packetClazz);
            } catch (Exception e) {
                throw new RunException("[class:{}][protocolId:{}] has no registration, please register for this protocol", packetClazz.getSimpleName(), protocolId);
            }

            try {
                AssertionUtils.isNull(receiverMap.get(protocolId), "duplicate protocol registration, @PacketReceiver [class:{}] is repeatedly received [at{}]", packetClazz.getSimpleName(), packetClazz.getSimpleName());

                PacketReceiverDefinition receiverDefinition = new PacketReceiverDefinition(bean, method, packetClazz, attachmentClazz);
                IPacketReceiver enhanceReceiverDefinition = EnhanceUtils.createPacketReceiver(receiverDefinition);
                receiverMap.put(protocolId, enhanceReceiverDefinition);
                logger.info("Register successfully! [class:{}] [method:{}] [packet:{}]", bean.getClass().getName(), methodName, packetName);
            } catch (Throwable t) {
                throw new RunException("Registration protocol [class:{}] unknown exception", packetClazz.getSimpleName(), t);
            }
        }
    }
}
