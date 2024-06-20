package com.helloclient.cross.service.user;

import com.awake.net2.rpc.anno.RpcService;
import com.awake.net2.rpc.client.AbstractGrpcClient;
import com.helloclient.cross.pojo.User;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author：lqh
 * @Date：2024/4/24 14:07
 */
@Service
@RpcService
public class UserClient extends AbstractGrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(UserClient.class);

    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    private static UserClient ins;
    @Override
    public void initStub() {
        blockingStub = UserServiceGrpc.newBlockingStub(getChannel());
        ins=this;
    }

    public static UserClient getIns() {
        return ins;
    }

    public void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        User.UserRequest request = User.UserRequest.newBuilder().setName(name).build();
        User.UserResponse response;
        try {
            response = blockingStub.findUserInfo(request);
        } catch (StatusRuntimeException e) {
            logger.info("RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: {}," , response.getName());
    }
}
