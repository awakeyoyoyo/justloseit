package com.hello.grpc.hello;


import com.hello.grpc.pojo.Hello;
import io.grpc.stub.StreamObserver;

import java.time.LocalDate;

/**
 * @Author：lqh
 * @Date：2024/4/11 16:36
 */
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void findUserInfo(Hello.UserRequest request, StreamObserver<Hello.UserResponse> responseObserver) {
        System.out.printf("receive search request ===  name:%s\n", request.getName());
        Hello.UserResponse response = Hello.UserResponse.newBuilder()
                .setBirthday(LocalDate.now().toString())
                .setCity("beijing").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
