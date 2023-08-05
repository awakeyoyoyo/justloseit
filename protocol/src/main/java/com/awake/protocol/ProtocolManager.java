package com.awake.protocol;

import com.awake.protocol.definition.ProtocolDefinition;
import com.awake.protocol.properties.ProtocolProperties;
import com.awake.util.ClassUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

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

    private ProtocolProperties protocolProperties;

    private HashMap<Integer, ProtocolDefinition> protocolDefinitionHashMap;

    public ProtocolManager(ProtocolProperties protocolProperties) {
        this.protocolProperties=protocolProperties;
    }

    @Override
    public ProtocolDefinition getProtocol(int protocolId) {
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        String scanProtocolPacket = protocolProperties.getScanProtocolPacket();
        //TODO 扫描出所有协议包
        System.out.println(scanProtocolPacket);

        Set<Class> classSet1 = ClassUtil.scanPackageClass("com.awake.protocol.packet");
        Set<Class> classSet2 = ClassUtil.scanPackageClass(scanProtocolPacket);
        System.out.println(classSet1);
        System.out.println(classSet2);
    }

    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public static void main(String[] args) {

        //TODO 扫描出所有协议包

        Set<Class> classSet = ClassUtil.scanPackageClass("com.awake.protocol.packet");
        System.out.println(classSet);
    }
}
