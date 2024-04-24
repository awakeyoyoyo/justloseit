package com.awake.net2.rpc;

import io.grpc.ManagedChannel;

/**
 * @Author：lqh
 * @Date：2024/3/29 14:44
 */
public interface IRpcManager {
    void start();

    void shutdown();

    ManagedChannel getRpcServiceChannel(int moduleId);

}
