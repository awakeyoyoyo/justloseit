package com.awake.net2.rpc;

import com.awake.exception.RunException;
import com.awake.net2.NetContext;
import com.awake.net2.rpc.anno.RpcServiceImpl;
import com.awake.net2.rpc.pojo.GrpcClientDefinition;
import com.awake.net2.rpc.pojo.ModuleIdAndPort;
import com.awake.net2.rpc.pojo.GrpcServiceDefinition;
import com.awake.net2.rpc.properties.RpcProperties;
import com.awake.net2.rpc.server.GrpcServer;
import com.awake.util.net.HostAndPort;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @Author：lqh
 * @Date：2024/3/29 14:45
 */
public class RpcManager implements IRpcManager, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcManager.class);

    @Resource
    private RpcProperties rpcProperties;

    /**
     * 提供服务grpcServer
     */
    private List<GrpcServer> grpcServerList = new ArrayList<>();
    /**
     * 服务名对应grpcServer
     */
    private Map<Integer, GrpcServer> moduleId2ServerMap = new HashMap<>();

    /**
     * 目标地址 对应服务列表
     */
    private Map<HostAndPort, List<GrpcClientDefinition>> hostAndPortListHashMap = new HashMap<>();

    /**
     * 模块id2channel
     */
    private Map<Integer, ManagedChannel> moduleId2Channel = new HashMap<>();

    private Map<HostAndPort, ManagedChannel> hostAndPortChannelMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化Rpc服务提供者信息
        initRpcServer();
        //初始化Rpc消费者信息
        initRpcClient();
    }


    //初始化
    private void initRpcClient() {
        var rpcConsumerHosts = rpcProperties.getRpcConsumerHosts();
        if (rpcConsumerHosts==null){
            logger.info("[RpcManager] no rpc service consume");
            return;
        }
        var moduleIdAndHostPorts = rpcConsumerHosts.split(",");
        if (moduleIdAndHostPorts.length==0){
            logger.info("[RpcManager] no rpc service consume");
            return;
        }
        Map<Integer, GrpcClientDefinition> moduleId2GrpcClientDefinition = new HashMap<>();
        for (String temp : moduleIdAndHostPorts) {
            var moduleIdAndHostPort = temp.split("_");
            var moduleId = Integer.parseInt(moduleIdAndHostPort[0]);
            var hostAndPort = HostAndPort.valueOf(moduleIdAndHostPort[1]);
            var grpcClientDefinition = new GrpcClientDefinition(moduleId, hostAndPort);
            moduleId2GrpcClientDefinition.put(moduleId, grpcClientDefinition);
        }

        for (Map.Entry<Integer, GrpcClientDefinition> entry : moduleId2GrpcClientDefinition.entrySet()) {
            GrpcClientDefinition grpcClientDefinition = entry.getValue();
            HostAndPort hostAndPort = grpcClientDefinition.getHostAndPort();
            hostAndPortListHashMap.computeIfAbsent(hostAndPort, k -> new ArrayList<>()).add(grpcClientDefinition);
        }

    }


    /**
     * 初始化Rpc服务提供
     *
     * @throws IOException
     */
    private void initRpcServer() throws IOException {
        var rpcProviderPort = rpcProperties.getRpcProviderPort();
        if (rpcProviderPort==null){
            logger.info("[RpcManager] no rpc service support");
            return;
        }
        var moduleIdAndPort = rpcProviderPort.split(",");
        if (moduleIdAndPort.length==0){
            logger.info("[RpcManager] no rpc service support");
            return;
        }
        Map<Integer, ModuleIdAndPort> moduleId2ServiceAndPortMap = new HashMap<>();
        for (String temp : moduleIdAndPort) {
            String[] serviceAndPortStr = temp.split("_");
            ModuleIdAndPort moduleIdAndPortPojo = new ModuleIdAndPort(Integer.parseInt(serviceAndPortStr[0]), Integer.parseInt(serviceAndPortStr[1]));
            moduleId2ServiceAndPortMap.put(moduleIdAndPortPojo.getModuleId(), moduleIdAndPortPojo);
        }
        //获取注解中的 服务提供类
        Map<Integer, List<GrpcServiceDefinition>> port2ServiceAndPortMap = new HashMap<>();
        var applicationContext = NetContext.getApplicationContext();
        var serviceBeans = applicationContext.getBeansWithAnnotation(RpcServiceImpl.class);
        for (var bean : serviceBeans.values()) {
            if (!(bean instanceof BindableService)) {
                throw new RunException("The RpcServiceImpl is no gprc impl, please check that [rpcServiceImpl class:{}] and application.properties", bean);
            }
            RpcServiceImpl rpcService = bean.getClass().getAnnotation(RpcServiceImpl.class);
            ModuleIdAndPort moduleIdAndPortPojo = moduleId2ServiceAndPortMap.get(rpcService.moduleId());
            if (moduleIdAndPortPojo == null) {
                throw new RunException("The RpcServiceImpl does not have port, please check that [rpcServiceImpl class:{}] and application.properties", bean);
            }
            var grpcServiceDefinition = new GrpcServiceDefinition(moduleIdAndPortPojo.getPort(), moduleIdAndPortPojo.getModuleId(), (BindableService) bean);
            port2ServiceAndPortMap.computeIfAbsent(moduleIdAndPortPojo.getPort(), k -> new ArrayList<>()).add(grpcServiceDefinition);
        }
        // 生成服务server
        for (Map.Entry<Integer, List<GrpcServiceDefinition>> entry : port2ServiceAndPortMap.entrySet()) {
            Integer port = entry.getKey();
            List<GrpcServiceDefinition> serviceList = entry.getValue();
            List<BindableService> services = new ArrayList<>();
            for (GrpcServiceDefinition grpcServiceDefinition : serviceList) {
                services.add(grpcServiceDefinition.getService());
            }
            GrpcServer grpcServer = new GrpcServer(port, services);
            grpcServerList.add(grpcServer);
            for (GrpcServiceDefinition grpcServiceDefinition : serviceList) {
                moduleId2ServerMap.put(grpcServiceDefinition.getModuleId(), grpcServer);
            }
        }
    }

    @Override
    public void start() {

        //启动服务提供
        for (GrpcServer grpcServer : grpcServerList) {
            try {
                grpcServer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        //启动客户端
        for (Map.Entry<HostAndPort, List<GrpcClientDefinition>> entry : hostAndPortListHashMap.entrySet()) {
            HostAndPort hostAndPort = entry.getKey();
            List<GrpcClientDefinition> grpcClientDefinitions = entry.getValue();
            for (GrpcClientDefinition grpcClientDefinition : grpcClientDefinitions) {
                ManagedChannel channel = hostAndPortChannelMap.computeIfAbsent(grpcClientDefinition.getHostAndPort(),
                        k -> Grpc.newChannelBuilder(hostAndPort.toHostAndPortStr(), InsecureChannelCredentials.create()).build());
                moduleId2Channel.put(grpcClientDefinition.getModuleId(), channel);
            }
        }


    }

    @Override
    public void shutdown() {
        for (GrpcServer grpcServer : grpcServerList) {
            grpcServer.stop();
        }

        for (ManagedChannel channel : hostAndPortChannelMap.values()) {
            channel.shutdown();
        }
    }
}
