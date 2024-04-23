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
        System.out.println("Server started, listening on " + server.getPort());
        //程序退出时关闭资源
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            GrpcServer.this.stop();
            System.err.println("*** server shut down");
        }));
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
