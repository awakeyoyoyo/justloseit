package com.awake.net.protocol;

import com.awake.net.protocol.anno.Packet;
import com.awake.net.protocol.definition.ProtocolDefinition;
import com.awake.net.protocol.properties.ProtocolProperties;
import com.awake.util.base.StringUtils;
import com.awake.util.clazz.ClassUtil;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

/**
 * @version : 1.0
 * @ClassName: ProtocolManager
 * @Description: 协议管理
 * @Auther: awake
 * @Date: 2023/8/3 21:10
 **/
@Data
public class ProtocolManager implements IProtocolManager, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolManager.class);

    public static final String ATTACHMENT_PACKET = "com.awake.net.router.attachment";
    public static final String COMMON_PACKET = "com.awake.net.packet.common";

    private ProtocolProperties protocolProperties;

    private HashMap<Integer, ProtocolDefinition> protocolDefinitionHashMap = new HashMap<>();

    public ProtocolManager(ProtocolProperties protocolProperties) {
        this.protocolProperties = protocolProperties;
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

    @Override
    public void afterPropertiesSet() {
        String scanProtocolPacket = protocolProperties.getScanProtocolPacket();
        String scanPackage = StringUtils.joinWith(StringUtils.COMMA, ATTACHMENT_PACKET, COMMON_PACKET, scanProtocolPacket);
        logger.info("[ProtocolManager] scan packages [{}]", scanPackage);
        Set<Class> packageClass = ClassUtil.scanPackageClass(scanPackage);
        if (packageClass.isEmpty()) {
            logger.warn("There are no protocol class.");
            return;
        }
        for (Class clazz : packageClass) {
            Annotation packetAnnotation = clazz.getAnnotation(Packet.class);
            if (packetAnnotation == null) {
                continue;
            }
            Annotation protoAnnotation = clazz.getAnnotation(ProtobufClass.class);
            if (protoAnnotation == null) {
                throw new IllegalArgumentException(StringUtils.format("[packet class:{}] must have a ProtobufClass anno!", clazz.getName()));
            }
            Packet packet = (Packet) packetAnnotation;
            if (protocolDefinitionHashMap.containsKey(packet.protocolId())) {
                throw new IllegalArgumentException(StringUtils.format("[packet class:{}] must have a unique protocolId : [{}]!", clazz.getName(), packet.protocolId()));
            }
            ProtocolDefinition protocolDefinition = ProtocolDefinition.valueOf(packet.protocolId(), clazz);
            //缓存
            ProtobufProxy.create(protocolDefinition.getProtocolClass());
            protocolDefinitionHashMap.put(packet.protocolId(), protocolDefinition);
        }
        if (protocolDefinitionHashMap.isEmpty()) {
            logger.warn("There are no protocolDefinitions.");
        }
        logger.info("register packet proto {}", protocolDefinitionHashMap.values());
    }

    public static void main(String[] args) {

        //TODO 扫描出所有协议包

        Set<Class> classSet = ClassUtil.scanPackageClass("com.awake.protocol.packet");
        System.out.println(classSet);
    }
}
