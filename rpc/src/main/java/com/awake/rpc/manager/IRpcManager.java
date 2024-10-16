package com.awake.rpc.manager;

import io.grpc.ManagedChannel;

/**
 * @Author：lqh
 * @Date：2024/3/29 14:44
 */
public interface IRpcManager {
    void init();

    void start();

    void shutdown();

    ManagedChannel getRpcServiceChannel(int moduleId);

}
