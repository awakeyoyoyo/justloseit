package com.hello.cross.user;

import com.awake.net2.rpc.anno.RpcServiceImpl;
import com.hello.cross.pojo.User;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

/**
 * @Author：lqh
 * @Date：2024/4/24 14:13
 */
@RpcServiceImpl
@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase{

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
        super.findUserByCity(request, responseObserver);
    }

    @Override
    public StreamObserver<User.UserInsertRequest> insertUser(StreamObserver<User.UserInsertResponse> responseObserver) {
        return super.insertUser(responseObserver);
    }

    @Override
    public StreamObserver<User.UserFilterRequest> listerUser(StreamObserver<User.UserFilterResponse> responseObserver) {
        return super.listerUser(responseObserver);
    }
}
