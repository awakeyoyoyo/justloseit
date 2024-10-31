package com.awake.rpc.manager;

import com.awake.rpc.pojo.GrpcClientDefinition;
import com.awake.rpc.properties.RpcProperties;
import com.awake.rpc.server.GrpcServer;
import com.awake.util.net.HostAndPort;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：lqh
 * @Date：2024/3/29 14:45
 */
public class RpcManager implements IRpcManager {
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

    /**
     * 地址2channel
     */
    private Map<HostAndPort, ManagedChannel> hostAndPortChannelMap = new HashMap<>();



    //初始化
    private void initRpcClient() {
        var rpcConsumerHosts = rpcProperties.getConsumerModuleIds();
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
    private void initRpcServer() {
    }

    @Override
    public void init() {
        //初始化Rpc服务提供者信息
        initRpcServer();
        //初始化Rpc消费者信息
        initRpcClient();
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

    @Override
    public ManagedChannel getRpcServiceChannel(int moduleId) {
        return moduleId2Channel.get(moduleId);
    }
}
