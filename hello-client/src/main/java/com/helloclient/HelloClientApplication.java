package com.helloclient;

import com.awake.net2.NetContext;
import com.awake.net2.server.tcp.TcpClient;
import com.awake.net2.session.Session;
import com.awake.net2.util.NetUtils;
import com.awake.util.net.HostAndPort;
import com.helloclient.cross.service.user.UserClient;
import com.helloclient.packet.LoginMsg;
import com.helloclient.protomodule.GameModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.helloclient.grpc.pojo.Hello world!
 */
@SpringBootApplication
public class HelloClientApplication {
    public static final int SERVER_PORT = 18000;

    public static void main(String[] args) {
        SpringApplication.run(HelloClientApplication.class, args);

        var tcpClient = new TcpClient(HostAndPort.valueOf(NetUtils.getLocalhostStr(), SERVER_PORT));
        Session session = tcpClient.start();
        System.out.println("hello world");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LoginMsg.RegisterRequest.Builder registerRequest = LoginMsg.RegisterRequest.newBuilder().setPassword("lqh777***")
                .setUserName("awakeyoyoyo4");
        NetContext.getRouter().send(session, GameModule.RegisterRequest,registerRequest.build());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LoginMsg.LoginRequest.Builder loginRequest = LoginMsg.LoginRequest.newBuilder().setPassword("lqh777***").setUserName("awakeyoyoyo3");
        NetContext.getRouter().send(session, GameModule.LoginRequest,loginRequest.build());

        UserClient.getIns().greet("lqhao");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
