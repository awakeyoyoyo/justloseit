package com.helloclient.grpc.user.client;

import com.helloclient.grpc.user.pojo.User;
import com.helloclient.grpc.user.service.UserServiceGrpc;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @Author：lqh
 * @Date：2024/4/17 15:28
 */
public class UserClient {
    private static final Logger logger = LoggerFactory.getLogger(UserClient.class);
    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;

    private final UserServiceGrpc.UserServiceStub asyncStub;

    /** Construct client for accessing HelloWorld server using the existing channel. */
    public UserClient(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = UserServiceGrpc.newBlockingStub(channel);
        asyncStub = UserServiceGrpc.newStub(channel);

    }


    /** Say hello to server. */
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
        logger.info("Greeting: " + response.getName());
    }
//
//    /** Say hello to server. */
//    public void asyncGreet(String name) {
//        logger.info("Will try to greet " + name + " ...");
//        User.UserRequest request = User.UserRequest.newBuilder().setName(name).build();
//        User.UserResponse response;
//        try {
//            StreamObserver<User.UserResponse> responseObserver
//            response = asyncStub.findUserInfo(request,);
//        } catch (StatusRuntimeException e) {
//            logger.info("RPC failed: {0}", e.getStatus());
//            return;
//        }
//        logger.info("Greeting: " + response.getName());
//    }

    public static void main(String[] args) throws Exception {
        String user = "world";
        // Access a service running on the local machine on port 50051
        String target = "localhost:50051";
        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        //
        // For the example we use plaintext insecure credentials to avoid needing TLS certificates. To
        // use TLS, use TlsChannelCredentials instead.
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        try {
            UserClient client = new UserClient(channel);
            client.greet(user);
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

    }
}
