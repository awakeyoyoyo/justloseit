package com.awake.net.router;

import com.awake.exception.RunException;
import com.awake.net.config.model.ProtocolModule;
import com.awake.net.router.attachment.GatewayAttachment;
import com.awake.net.router.attachment.IAttachment;
import com.awake.net.router.receiver.*;
import com.awake.net.session.Session;
import com.awake.util.AssertionUtils;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.ArrayUtils;
import com.awake.util.base.StringUtils;
import com.awake.util.clazz.ClassUtil;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @version : 1.0
 * @ClassName: PacketBus
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/6 11:20
 **/
public class PacketManager implements IPacketManager {
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

    public static final byte MAX_MODULE_NUM = Byte.MAX_VALUE;
    public static final short MAX_PROTOCOL_NUM = Short.MAX_VALUE;

    private static final Logger logger = LoggerFactory.getLogger(PacketManager.class);

    private static final Map<Integer, IPacketReceiver> receiverMap = new HashMap<>();

    private HashMap<Integer, ProtocolDefinition> protocolDefinitionHashMap = new HashMap<>();

    private static HashMap<String, Integer> protocolName2ProtocolIdHashMap = new HashMap<>();
    /**
     * The protocol corresponding to the protocolId.(协议号protocolId对应的协议，数组下标是协议号protocolId)
     */
//    public static final IProtocolRegistration[] protocols = new IProtocolRegistration[MAX_PROTOCOL_NUM];
    /**
     * The modules of the protocol.(协议的模块)
     */
    public static final ProtocolModule[] modules = new ProtocolModule[MAX_MODULE_NUM];

    public static final ProtocolDefinition[] protocols = new ProtocolDefinition[MAX_PROTOCOL_NUM];


    public static final String ATTACHMENT_PACKET = "com.awake.net.router.attachment";
    public static final String COMMON_PACKET = "com.awake.net.packet.common";
    public static final String GATEWAY_PACKET = "com.awake.net.gateway.core.packet";

    @Override
    public void init(ApplicationContext applicationContext) {


        String scanCommonPackage = StringUtils.joinWith(StringUtils.COMMA, GATEWAY_PACKET, ATTACHMENT_PACKET, COMMON_PACKET);
        Set<Class> packageClass = ClassUtil.scanPackageClass(scanCommonPackage);
        //註冊基礎協議包 和信號包
        for (Class packageClazz : packageClass) {
            if (packageClazz.getAnnotation(ProtobufClass.class)!=null) {
                registerCommonProtoClass(packageClazz);
            }
        }


        // 注册协议接收器
        Map<String, Object> componentBeans = applicationContext.getBeansWithAnnotation(PacketController.class);
        for (Object bean : componentBeans.values()) {
            registerPacketReceiverDefinition(bean);
        }
    }



    @Override
    public IPacketReceiver getPacketReceiver(Object packet) {
        IPacketReceiver receiver = receiverMap.get(getProtocolId(packet.getClass()));
        if (receiver == null) {
            String name = packet.getClass().getSimpleName();
            throw new RuntimeException(StringUtils.format("no any packetReceiver:[at{}] found for this packet:[{}] or no GatewayAttachment sent back if this server is gateway", name, name));
        }
        return receiver;
    }


    public void registerPacketReceiverDefinition(Object bean) {
        Class<?> clazz = bean.getClass();

        Method[] methods = ReflectionUtils.getMethodsByAnnoInPOJOClass(clazz, PacketReceiver.class);

        AssertionUtils.isTrue(ArrayUtils.isNotEmpty(methods), "[class:{}] ,the PacketController  class must be had PacketReceiver  method.", bean.getClass().getName());

        if (!ReflectionUtils.isPojoClass(clazz)) {
            logger.warn("The message registration class [{}] is not a POJO class, and the parent class will not be scanned", clazz);
        }

        for (Method method : methods) {
            Class<?>[] paramClazzs = method.getParameterTypes();

            AssertionUtils.isTrue(paramClazzs.length == 2 || paramClazzs.length == 3, "[class:{}] [method:{}] must have two or three parameter!", bean.getClass().getName(), method.getName());

            AssertionUtils.isTrue(Session.class.isAssignableFrom(paramClazzs[0]), "[class:{}] [method:{}],the first parameter must be Session type parameter Exception.", bean.getClass().getName(), method.getName());

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
            PacketController packetController = clazz.getAnnotation(PacketController.class);
            PacketReceiver packetReceiver = method.getAnnotation(PacketReceiver.class);
            int protocolId = packetReceiver.protocolId();
            int moduleId = packetController.moduleId();

            registerPacketClass(packetClazz, protocolId, moduleId);
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

    /**
     * 註冊通用包和信號包
     * @param packetClazz
     */
    private void registerCommonProtoClass(Class<?> packetClazz) {
        int protocolId = 0;
        try {
            Field field = packetClazz.getField("PROTOCOL_ID");
            AssertionUtils.isTrue(field != null, "[class:{}] ，CommonProtoClass class must had PROTOCOL_ID field", packetClazz.getName());
            protocolId = field.getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RunException("Registration commom proto class [class:{}] unknown exception", packetClazz.getSimpleName(), e);
        }
        AssertionUtils.isTrue(protocolId != 0, "[class:{}] ，CommonProtoClass class must had protocolId.", packetClazz.getName());

        ProtocolDefinition protocolDefinition = ProtocolDefinition.valueOf(protocolId, packetClazz);
        //缓存
        ProtobufProxy.create(packetClazz);
        //注册协议
        protocolDefinitionHashMap.put(protocolId, protocolDefinition);
        protocols[protocolId] = protocolDefinition;

        protocolName2ProtocolIdHashMap.put(packetClazz.getSimpleName(), protocolId);
    }

    /**
     * 註冊協議
     * @param packetClazz
     * @param protocolId
     * @param moduleId
     */
    private void registerPacketClass(Class<?> packetClazz, int protocolId, int moduleId) {
        ProtocolDefinition protocolDefinition = ProtocolDefinition.valueOf(protocolId, moduleId, packetClazz);
        //缓存
        ProtobufProxy.create(packetClazz);
        //注册协议
        protocolDefinitionHashMap.put(protocolId, protocolDefinition);
        protocols[protocolId] = protocolDefinition;
        //注册模块->
        if (modules[moduleId] == null) {
            modules[moduleId] = new ProtocolModule(moduleId, "");
        }
        protocolName2ProtocolIdHashMap.put(packetClazz.getSimpleName(), protocolId);
    }


    public static ProtocolModule moduleByProtocolId(int protocolId) {
        return modules[protocols[protocolId].getModule()];
    }

    public static ProtocolModule moduleByProtocol(Class<?> clazz) {
        return moduleByProtocolId(protocolId(clazz));
    }

    /**
     * Find the module based on the module ID
     */
    public static ProtocolModule moduleByModuleId(int moduleId) {
        var module = modules[moduleId];
        AssertionUtils.notNull(module, "[moduleId:{}]不存在", moduleId);
        return module;
    }


    public static int protocolId(Class<?> clazz) {
        return protocolName2ProtocolIdHashMap.get(clazz.getSimpleName());
    }

    @Override
    public ProtocolDefinition getProtocolDefinition(int protocolId) {
        return protocolDefinitionHashMap.get(protocolId);
    }

    @Override
    public int getProtocolId(Class<?> packetClazz) {
        for (ProtocolDefinition definition : protocolDefinitionHashMap.values()) {
            if (definition.getProtocolClass().equals(packetClazz)){
                return definition.getProtocolId();
            }
        }
        return 0;
    }

    public HashMap<Integer, ProtocolDefinition> getProtocolDefinitionHashMap() {
        return protocolDefinitionHashMap;
    }
}
