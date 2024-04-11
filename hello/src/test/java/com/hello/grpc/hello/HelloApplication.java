package com.hello.grpc.hello;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @Author：lqh
 * @Date：2024/4/11 17:02
 */
public class HelloApplication {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8088)
                .addService(new UserServiceImpl()).build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            stopServer(server);
            System.err.println("*** server shut down");
        }));

        server.awaitTermination();
    }

    private static void stopServer(Server server) {
        if (server != null) {
            server.shutdown();
        }
    }

}
