package com.awake.net2.rpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.List;

/**
 * grpc server
 * @Author：lqh
 * @Date：2024/4/23 15:55
 */
public class GrpcServer {

    private Server server;

    public GrpcServer(int port, List<BindableService> services) throws IOException {
        //初始化Server参数
        ServerBuilder builder = ServerBuilder.forPort(port);
        for(BindableService bs:services){
            builder.addService(bs);
        }
        server = builder.build();
    }

    /**
     * 启动服务
     */
    public void start() throws IOException {
        server.start();
    }

    /**
     * 关闭服务
     */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * 使得server一直处于运行状态
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
