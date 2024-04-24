package com.awake.net2.rpc.client;

import io.grpc.ManagedChannel;

import java.util.concurrent.TimeUnit;

/**
 * @Author：lqh
 * @Date：2024/4/23 16:00
 */
public abstract class GrpcClient {

    private ManagedChannel channel;



    /**
     * 关闭方法
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

}
