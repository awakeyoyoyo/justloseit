package com.hello.grpc.user.service;

import com.hello.grpc.user.pojo.User;
import io.grpc.stub.StreamObserver;

/**
 * @Author：lqh
 * @Date：2024/4/12 15:33
 */
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase{

    public static int roleNum=0;
    @Override
    public void findUserInfo(User.UserRequest request, StreamObserver<User.UserResponse> responseObserver) {
        responseObserver.onNext(handlerFindUser(request));
        responseObserver.onCompleted();
    }

    private User.UserResponse handlerFindUser(User.UserRequest request) {
        return User.UserResponse.newBuilder().setName("lqh").setBirthday("1998-07-16")
                .setCity("ShunDe").build();
    }

    @Override
    public void findUserByCity(User.UserFindRequest request, StreamObserver<User.UserFindResponse> responseObserver) {
        for (int i = 0; i < 10; i++) {
            responseObserver.onNext(handlerFindUserByCity(i));
        }
        responseObserver.onCompleted();
    }

    private User.UserFindResponse handlerFindUserByCity(int i) {
        return User.UserFindResponse.newBuilder().setName("lqh" + i).setBirthday("1998-07-16")
                .setCity("ShunDe" + i).build();
    }

    @Override
    public StreamObserver<User.UserInsertRequest> insertUser(StreamObserver<User.UserInsertResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(User.UserInsertRequest userInsertRequest) {
                roleNum++;
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(User.UserInsertResponse.newBuilder().setUserNum(roleNum).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<User.UserFilterRequest> listerUser(StreamObserver<User.UserFilterResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(User.UserFilterRequest userInsertRequest) {
                roleNum++;
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(User.UserFilterResponse.newBuilder().setUserNum(roleNum).build());
                responseObserver.onCompleted();
            }
        };
    }
}
