package com.hello;

import com.awake.net2.server.tcp.TcpServer;
import com.awake.net2.util.NetUtils;
import com.awake.util.net.HostAndPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

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
    }
}
