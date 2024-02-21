package com.awake.net.protocol;

import com.awake.net.config.model.ProtocolModule;
import com.awake.net.protocol.definition.ProtocolDefinition;
import com.awake.net.protocol.properties.ProtocolProperties;
import com.awake.orm.model.Pair;
import com.awake.util.AssertionUtils;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    public static final byte MAX_MODULE_NUM = Byte.MAX_VALUE;
    public static final short MAX_PROTOCOL_NUM = Short.MAX_VALUE;

    private static final Logger logger = LoggerFactory.getLogger(ProtocolManager.class);

    public static final String ATTACHMENT_PACKET = "com.awake.net.router.attachment";
    public static final String COMMON_PACKET = "com.awake.net.packet.common";
    public static final String GATEWAY_PACKET = "com.awake.net.gateway.core.packet";

    public static final String COMMON_MODULE = "com.awake.net.module";

    public static final String MODULE_NAME_RULE = "Module";

    public static final String MODULE_ID_NAME_RULE = "ModuleId";
    @Resource
    private ProtocolProperties protocolProperties;

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

    public ProtocolManager(ProtocolProperties protocolProperties) {
        this.protocolProperties = protocolProperties;
    }

    public ProtocolManager() {
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
            if (definition.getProtocolClass().equals(packetClazz)) {
                return definition.getProtocolId();
            }
        }
        return 0;
    }

    @Override
    public void afterPropertiesSet() {
        String scanProtocolPacket = protocolProperties.getScanProtocolPacket();
        String scanProtocolPackages = StringUtils.joinWith(StringUtils.COMMA, GATEWAY_PACKET, ATTACHMENT_PACKET, COMMON_PACKET, scanProtocolPacket);
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
        Map<String, Pair<Integer, Integer>> protocolName2ModuleIdAndProtocolIdMap = new HashMap<>();
        for (Class<?> moduleClazz : moduleClass) {
            //模块id
            String moduleName = StringUtils.substringBeforeFirst(moduleClazz.getSimpleName(), MODULE_NAME_RULE);
            Field moduleIdField = ReflectionUtils.getFieldByNameInPOJOClass(moduleClazz, MODULE_ID_NAME_RULE);
            var moduleId = (int) ReflectionUtils.getField(moduleIdField, null);

            Field[] fields = moduleClazz.getDeclaredFields();
            for (Field field : fields) {
                String fileName = field.getName();
                if (fileName.contains(MODULE_ID_NAME_RULE)) {
                    continue;
                }
                int protocolId = (int) ReflectionUtils.getField(field, null);
                if (protocolName2ModuleIdAndProtocolIdMap.containsKey(fileName)) {
                    throw new IllegalArgumentException(StringUtils.format("[protocol class:{}]  duplicate protocol registration", fileName));
                }
                protocolName2ModuleIdAndProtocolIdMap.put(fileName, new Pair(moduleId, protocolId));
            }
            //注册模块->
            if (modules[moduleId] == null) {
                modules[moduleId] = new ProtocolModule(moduleId, moduleName);
            }
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
            protocols[protocolId] = protocolDefinition;
            protocolName2ProtocolIdHashMap.put(clazz.getSimpleName(), protocolId);
        }
        if (protocolDefinitionHashMap.isEmpty()) {
            logger.warn("There are no protocolDefinitions.");
        }
        for (ProtocolDefinition protocolDefinition : protocolDefinitionHashMap.values()) {
            logger.info("register packet :[{}]", protocolDefinition);
        }
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


    public static void main(String[] args) {

        //TODO 扫描出所有协议包

        Set<Class> classSet = ClassUtil.scanPackageClass("com.awake.protocol.packet");
        System.out.println(classSet);
    }
}
