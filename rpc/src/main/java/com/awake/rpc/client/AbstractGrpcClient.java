package com.awake.rpc.client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractBlockingStub;
import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/4/23 16:00
 */
@Data
public abstract class AbstractGrpcClient<T extends AbstractBlockingStub> {

    /**
     * 初始化存根
     */
    public abstract T initStub(Channel channel);

    /**
     * 获取存根 先从缓存中获取 没有再构建。
     */
    public abstract T getStub();
}
