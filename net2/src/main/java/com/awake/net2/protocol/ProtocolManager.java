package com.awake.net2.protocol;

import com.awake.net2.protocol.definition.ProtocolDefinition;
import com.awake.net2.protocol.properties.ProtocolProperties;
import com.awake.orm.model.Pair;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.StringUtils;
import com.awake.util.clazz.ClassUtil;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author：lqh
 * @Date：2024/3/15 14:10
 */
public class ProtocolManager implements IProtocolManager, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolManager.class);

    public static final String COMMON_PACKET = "com.awake.net2.packet.common";

    public static final String COMMON_MODULE = "com.awake.net2.module";

    public static final String MODULE_NAME_RULE = "Module";

    public static final String MODULE_ID_NAME_RULE = "ModuleId";

    private static HashMap<Integer, ProtocolDefinition> protocolDefinitionHashMap = new HashMap<>();

    private static HashMap<Integer, Set<Integer>> moduleId2ProtocolId=new HashMap<>();

    private static HashMap<String, Integer> protocolName2ProtocolIdHashMap = new HashMap<>();
    @Resource
    private ProtocolProperties protocolProperties;

    public ProtocolManager(ProtocolProperties protocolProperties) {
        this.protocolProperties = protocolProperties;
    }

    public ProtocolManager() {
    }

    @Override
    public ProtocolDefinition getProtocolDefinition(int protocolId) {
        return protocolDefinitionHashMap.get(protocolId);
    }

    @Override
    public int getProtocolId(Class<?> packetClazz) {
        return protocolName2ProtocolIdHashMap.get(packetClazz.getSimpleName());
    }


    @Override
    public void afterPropertiesSet() {
        String scanProtocolPacket = protocolProperties.getScanProtocolPacket();
        String scanProtocolPackages = StringUtils.joinWith(StringUtils.COMMA, COMMON_PACKET, scanProtocolPacket);
        logger.info("[ProtocolManager] scan protocol packages [{}]", scanProtocolPackages);
        Set<Class> packageClass = ClassUtil.scanPackageClass(scanProtocolPackages);

        String scanModulePacket = protocolProperties.getScanModulePacket();
        String scaModulePackages = StringUtils.joinWith(StringUtils.COMMA, COMMON_MODULE, scanModulePacket);
        Set<Class> moduleClass = scanModuleClass(scaModulePackages);
        logger.info("[ProtocolManager] scan module packages [{}]", scaModulePackages);
        if (packageClass.isEmpty()) {
            logger.warn("There are no protocol class.");
            return;
        }
        if (moduleClass.isEmpty()) {
            logger.warn("There are no module class.");
            return;
        }
        Set<String> protocolNameSet = new HashSet<>();
        Set<Integer> protocolIdSet = new HashSet<>();
        Set<Integer> moduleIdSet = new HashSet<>();
        Map<String, Pair<Integer, Integer>> protocolName2ModuleIdAndProtocolIdMap = new HashMap<>();
        for (Class<?> moduleClazz : moduleClass) {
            //模块id
            String moduleName = StringUtils.substringBeforeFirst(moduleClazz.getSimpleName(), MODULE_NAME_RULE);
            Field moduleIdField = ReflectionUtils.getFieldByNameInPOJOClass(moduleClazz, MODULE_ID_NAME_RULE);
            var moduleId = (int) ReflectionUtils.getField(moduleIdField, null);
            if (moduleIdSet.contains(moduleId)){
                throw new IllegalArgumentException(StringUtils.format("[moduleName:{}]  duplicate moduleId registration", moduleName));
            }
            moduleIdSet.add(moduleId);
            Field[] fields = moduleClazz.getDeclaredFields();

            Set<Integer> moduleProtocolIds = new HashSet<>();
            for (Field field : fields) {
                String fileName = field.getName();
                if (fileName.contains(MODULE_ID_NAME_RULE)) {
                    continue;
                }
                int protocolId = (int) ReflectionUtils.getField(field, null);
                if (protocolNameSet.contains(fileName)||protocolIdSet.contains(protocolId)) {
                    throw new IllegalArgumentException(StringUtils.format("[protocol class:{}]  duplicate protocol registration", fileName));
                }
                if (protocolName2ModuleIdAndProtocolIdMap.containsKey(fileName)) {
                    throw new IllegalArgumentException(StringUtils.format("[protocol class:{}]  duplicate protocol registration", fileName));
                }
                protocolName2ModuleIdAndProtocolIdMap.put(fileName, new Pair(moduleId, protocolId));
                protocolNameSet.add(fileName);
                protocolIdSet.add(protocolId);
                moduleProtocolIds.add(protocolId);
            }
            //注册模块->
            moduleId2ProtocolId.put(moduleId, moduleProtocolIds);
        }

        for (Class clazz : packageClass) {
            //适配 拥有接口的协议类
            if (clazz.isInterface()) {
                continue;
            }
            if (clazz.isEnum()) {
                continue;
            }
            Annotation protoAnnotation = clazz.getAnnotation(ProtobufClass.class);
            if (protoAnnotation == null) {
                continue;
//                throw new IllegalArgumentException(StringUtils.format("[packet class:{}] must have a ProtobufClass anno!", clazz.getName()));
            }
            var moduleIdAndProtocolIdPair = protocolName2ModuleIdAndProtocolIdMap.get(clazz.getSimpleName());
            if (moduleIdAndProtocolIdPair == null) {
                throw new IllegalArgumentException(StringUtils.format("[packet class:{}] must have a moduleId nad protocolId !", clazz.getName()));
            }
            var protocolId = moduleIdAndProtocolIdPair.getValue();
            var moduleId = moduleIdAndProtocolIdPair.getKey();

            if (protocolDefinitionHashMap.containsKey(protocolId)) {
                throw new IllegalArgumentException(StringUtils.format("[packet class:{}] must have a unique protocolId : [{}]!", clazz.getName(), protocolId));
            }
            ProtocolDefinition protocolDefinition = ProtocolDefinition.valueOf(protocolId, moduleId, clazz);
            //缓存
            ProtobufProxy.create(protocolDefinition.getProtocolClass());
            //注册协议
            protocolDefinitionHashMap.put(protocolId, protocolDefinition);
            protocolName2ProtocolIdHashMap.put(clazz.getSimpleName(), protocolId);
        }
        if (protocolDefinitionHashMap.isEmpty()) {
            logger.warn("There are no protocolDefinitions.");
        }
//        for (ProtocolDefinition protocolDefinition : protocolDefinitionHashMap.values()) {
//            logger.info("register packet :[{}]", protocolDefinition);
//        }
    }

    private Set<Class> scanModuleClass(String scaModulePackages) {
        Set<Class> result=new HashSet<>();
        Set<Class> moduleClass = ClassUtil.scanPackageClass(scaModulePackages);
        for (Class moduleClazz : moduleClass) {
            if (!moduleClazz.getSimpleName().contains(MODULE_NAME_RULE)){
                continue;
            }
            result.add(moduleClazz);
        }
        return moduleClass;
    }

    public static HashMap<Integer, ProtocolDefinition> getProtocolDefinitionHashMap() {
        return protocolDefinitionHashMap;
    }

    public static HashMap<Integer, Set<Integer>> getModuleId2ProtocolId() {
        return moduleId2ProtocolId;
    }
}
