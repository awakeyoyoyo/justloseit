package com.awake.thread.pool.model;

import com.awake.util.math.RandomUtils;
import com.awake.util.base.ThreadUtils;
import io.netty.channel.DefaultEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @version : 1.0
 * @ClassName: SingleThreadActorPool
 * @Description: 基于单独线程执行实现的Actor模型
 * @Auther: awake
 * @Date: 2023/3/10 11:24
 **/
public class ThreadActorPoolModel implements IThreadPoolModel {

    private static final Logger logger = LoggerFactory.getLogger(ThreadActorPoolModel.class);

    private ExecutorService[] executors;

    public int executorsSize;


    public ThreadActorPoolModel(int executorsSize) {
        this.executorsSize = executorsSize;
        executors = new ExecutorService[executorsSize];
        for (int i = 0; i < executorsSize; i++) {
            //使用netty自带
            DefaultEventLoop executor = new DefaultEventLoop();
            executors[i] = executor;
        }
    }

    public int calTaskExecutorHash(int taskExecutorHash) {
        // Other hash algorithms can be customized to make the distribution more uniform
        return Math.abs(taskExecutorHash) % executorsSize;
    }

    public int calTaskExecutorHash(Object argument) {
        int hash = 0;
        if (argument == null) {
            hash = RandomUtils.randomInt();
        } else if (argument instanceof Number) {
            hash = ((Number) argument).intValue();
        } else {
            hash = argument.hashCode();
        }
        return calTaskExecutorHash(hash);
    }

    @Override
    public void execute(int executorHash, Runnable runnable) {
        executors[Math.abs(executorHash % executorsSize)].execute(ThreadUtils.safeRunnable(runnable));
    }


    @Override
    public CompletableFuture asyncExecuteCallable(int callBackExecutorHash, int taskExecutorHash, Callable callable) {
        Callable safeCallable = ThreadUtils.safeCallable(callable);
        ExecutorService executor = executors[Math.abs(taskExecutorHash % executorsSize)];
        CompletableFuture resultFuture = new CompletableFuture();
        executor.execute(() -> {
            Object result;
            try {
                result = safeCallable.call();
                ExecutorService callBackExecutor = executors[Math.abs(callBackExecutorHash % executorsSize)];
                resultFuture.completeAsync(() -> result, callBackExecutor);
            } catch (Exception e) {
                logger.error("[ThreadActorPoolModel] asyncExecuteCallable run error, error msg:{}", e.getMessage());
                e.printStackTrace();
            }
        });
        return resultFuture;
    }

    @Override
    public CompletableFuture asyncExecuteCallable(int taskExecutorHash, Callable callable) {
        return asyncExecuteCallable(RandomUtils.randomInt(), taskExecutorHash, callable);
    }

    /**
     * 请求成功过后依然在相同的线程执行回调任务
     */
    public Executor currentThreadExecutor() {

        var threadId = Thread.currentThread().getId();

        return executors[calTaskExecutorHash(RandomUtils.randomInt())];
    }
}
