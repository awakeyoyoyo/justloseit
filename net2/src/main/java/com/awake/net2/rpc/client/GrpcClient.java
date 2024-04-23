package com.awake.net2.rpc.client;

import io.grpc.ManagedChannel;

/**
 * @Author：lqh
 * @Date：2024/4/23 16:00
 */
public class GrpcClient {
//    private static ManagedChannel channel;//grpc信道,需要指定端口和地址
//    private static CommonServiceGrpc.CommonServiceBlockingStub blockingStub;//阻塞/同步存根
//    private static CommonServiceGrpc.CommonServiceStub asyncStub;//非阻塞,异步存根
//    public GrpcClient(String server, int port) {
//        System.out.println("server:"+server+",port:"+port);
//        //创建信道
//        channel = ManagedChannelBuilder.forAddress(server, port)
//                .usePlaintext()
//                .build();
//        //创建存根
//        blockingStub = CommonServiceGrpc.newBlockingStub(channel);
//        asyncStub = CommonServiceGrpc.newStub(channel);
//    }
//
//    /**
//     * 关闭方法
//     */
//    public void shutdown() throws InterruptedException {
//        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
//    }
//
}
