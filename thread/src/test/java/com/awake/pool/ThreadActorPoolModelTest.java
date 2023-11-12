package com.awake.pool;

import com.awake.thread.pool.model.ThreadActorPoolModel;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @version : 1.0
 * @ClassName: ThreadActorPoolModelTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/13 16:55
 **/
public class ThreadActorPoolModelTest {

    @Test
    public void threadActorPoolModelAsyncTest() throws IOException {
        ThreadActorPoolModel poolModel = new ThreadActorPoolModel(10);
        poolModel.execute(1, () -> {
            System.out.println("[发送异步指令-测试线程执行]:" + Thread.currentThread());
            CompletableFuture future = poolModel.asyncExecuteCallable(1,2,() -> {
                System.out.println("[执行异步指令-测试线程执行]:" + Thread.currentThread());
                Thread.sleep(1000);
                return 1;
            });
            future.whenComplete((obj,err)->{
                if(err!=null){
                    System.out.println("errorrrrrrrrr");
                }
                int res= (int) obj;
                System.out.println("[res]:" + res);
                System.out.println("[异步指令回调-测试线程执行]:" + Thread.currentThread());
            });
        });
        System.out.println("[发送异步指令完毕-测试线程执行]:" + Thread.currentThread());
//        System.in.read();
    }

    @Test
    public void threadActorPoolModelSyncTest() throws IOException {
        ThreadActorPoolModel poolModel = new ThreadActorPoolModel(10);
        poolModel.execute(1, () -> {
            System.out.println("[发送异步指令-测试线程执行]:" + Thread.currentThread());
            CompletableFuture future = poolModel.asyncExecuteCallable(2, () -> {
                System.out.println("[执行异步指令-测试线程执行]:" + Thread.currentThread());
                throw  new RuntimeException("ggbond");
            });
            try {
                Object o = future.get();
                System.out.println("[同步指令回调-测试线程执行]:" + Thread.currentThread());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        });
        System.out.println("[发送异步指令完毕-测试线程执行]:" + Thread.currentThread());
//        System.in.read();
    }
}
