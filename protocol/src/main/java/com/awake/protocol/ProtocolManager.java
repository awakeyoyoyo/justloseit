package com.awake.protocol;

import com.awake.protocol.anno.Packet;
import com.awake.protocol.definition.ProtocolDefinition;
import com.awake.protocol.properties.ProtocolProperties;
import com.awake.util.ClassUtil;
import com.awake.util.StringUtils;
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
        Set<Class> packageClass = ClassUtil.scanPackageClass(scanProtocolPacket);
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
            protocolDefinitionHashMap.put(packet.protocolId(), protocolDefinition);
        }
        if (protocolDefinitionHashMap.isEmpty()) {
            logger.warn("There are no protocolDefinitions.");
        }
    }

    public static void main(String[] args) {

        //TODO 扫描出所有协议包

        Set<Class> classSet = ClassUtil.scanPackageClass("com.awake.protocol.packet");
        System.out.println(classSet);
    }
}
