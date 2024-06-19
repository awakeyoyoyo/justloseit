package com.hello.cross.provider.user;

import com.awake.net2.router.Router;
import com.awake.net2.router.TaskBus;
import com.awake.net2.rpc.anno.RpcServiceImpl;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author：lqh
 * @Date：2024/4/24 14:13
 */
@RpcServiceImpl
@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public void findUserInfo(User.UserRequest request, StreamObserver<User.UserResponse> responseObserver) {
        logger.info("UserServiceImpl atFindUserInfo");
        TaskBus.execute(10, () -> {
            responseObserver.onNext(handlerFindUser(request));
            responseObserver.onCompleted();
            logger.info("UserServiceImpl execute findUserInfo");
        });
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
