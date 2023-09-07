package com.awake.zookeeper.completable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @version : 1.0
 * @ClassName: CompletableFutureTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 10:45
 **/
public class CompletableFutureTest {
    public static void main(String[] args) throws IOException {
        System.out.println("current:" + Thread.currentThread().getName());
        CompletableFuture result = new CompletableFuture<>();

        CompletableFuture result1 = new CompletableFuture<>();

        result.whenCompleteAsync((answer, throwE) -> {
            System.out.println("whenCompleteAsync:" + Thread.currentThread().getName());
        });

        result1.whenComplete((answer, throwE) -> {
            System.out.println("whenComplete:" + Thread.currentThread().getName());
        });

        result.complete(1);
        result1.complete(1);
        System.in.read();
    }
}
