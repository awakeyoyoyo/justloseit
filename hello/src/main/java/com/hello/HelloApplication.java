package com.hello;

import com.awake.net2.server.tcp.TcpServer;
import com.awake.net2.util.NetUtils;
import com.awake.util.net.HostAndPort;
import com.hello.cross.hello.UserServiceGrpc;
import com.hello.cross.hello.UserServiceImpl;
import com.hello.cross.pojo.Hello;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class HelloApplication
{
    public static final int SERVER_PORT = 18000;

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(HelloApplication.class, args);
        System.out.println("hello world");
        var tcpServer = new TcpServer(HostAndPort.valueOf(NetUtils.getLocalhostStr(), SERVER_PORT));
        tcpServer.start();

        Server server = ServerBuilder.forPort(8088)
                .addService(new UserServiceImpl()).build();
        server.start();
        server.awaitTermination();
    }
}
