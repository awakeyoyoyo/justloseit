package com.awake.net2.rpc.client;

import com.awake.net2.NetContext;
import io.grpc.ManagedChannel;
import lombok.Data;

/**
 * @Author：lqh
 * @Date：2024/4/23 16:00
 */
@Data
public abstract class AbstractGrpcClient {

    private ManagedChannel channel;

    public abstract void initStub();
}
