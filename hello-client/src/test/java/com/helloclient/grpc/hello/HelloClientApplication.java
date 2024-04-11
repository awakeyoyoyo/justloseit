package com.helloclient.grpc.hello;

import com.helloclient.grpc.pojo.Hello;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Author：lqh
 * @Date：2024/4/11 17:03
 */
public class HelloClientApplication {

    public static void main(String[] args) throws InterruptedException {
        //测试Grpc
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8088)
                .usePlaintext()
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);
        Hello.UserRequest request = Hello.UserRequest.newBuilder()
                .setName("jay chou")
                .build();

        Hello.UserResponse response = stub.findUserInfo(request);
        System.out.println(response);


        channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);

    }
}
