package com.awake.thread.pool.model;

import com.awake.util.math.RandomUtils;
import com.awake.util.base.ThreadUtils;
import io.netty.channel.DefaultEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @version : 1.0
 * @ClassName: SingleThreadActorPool
 * @Description: 基于单独线程执行实现的Actor模型
 * @Auther: awake
 * @Date: 2023/3/10 11:24
 **/
public class ThreadActorPoolModel implements IThreadPoolModel {

    private static final Logger logger = LoggerFactory.getLogger(ThreadActorPoolModel.class);

    /**
     * 内部线程池
     */
    private final ExecutorService[] executors;

    /**
     * 线程池大小
     */
    public int poolSize;

    /**
     * 线程id对应线程池
     */
    private final ConcurrentHashMap<Long, ExecutorService> threadId2ExecutorMap = new ConcurrentHashMap<>();

    public ThreadActorPoolModel(int poolSize) {
        this.poolSize = poolSize;
        executors = new ExecutorService[poolSize];
        for (int i = 0; i < poolSize; i++) {
            //使用netty自带
            DefaultEventLoop executor = new DefaultEventLoop();
            executors[i] = executor;
        }
        //走捷径获取线程id
        for (ExecutorService executor : executors) {
            executor.submit(() -> {
                threadId2ExecutorMap.put(Thread.currentThread().getId(), executor);
                ThreadUtils.registerSingleThreadExecutor(Thread.currentThread(), executor);
            });
        }
    }

    /**
     * 计算hash
     *
     * @param taskExecutorHash
     * @return
     */
    public int calTaskExecutorHash(int taskExecutorHash) {
        // Other hash algorithms can be customized to make the distribution more uniform
        return Math.abs(taskExecutorHash) % poolSize;
    }

    /**
     * 计算hash
     *
     * @param argument
     * @return
     */
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
        executors[calTaskExecutorHash(executorHash)].execute(ThreadUtils.safeRunnable(runnable));
    }


    @Override
    public CompletableFuture<?> asyncExecuteCallable(int callBackExecutorHash, int taskExecutorHash, Callable<?> callable) {
        ExecutorService executor = executors[calTaskExecutorHash(taskExecutorHash)];
        CompletableFuture<Object> resultFuture = new CompletableFuture<>();
        executor.execute(() -> {
            Object result;
            try {
                result = ThreadUtils.safeCallable(callable).call();
                ExecutorService callBackExecutor = executors[calTaskExecutorHash(callBackExecutorHash)];
                resultFuture.completeAsync(() -> result, callBackExecutor);
            } catch (Exception e) {
                logger.error("[ThreadActorPoolModel] asyncExecuteCallable run error, error msg:{}", e.getMessage());
                e.printStackTrace();
                resultFuture.completeExceptionally(e); // 添加异常传递
            }
        });
        return resultFuture;
    }

    @Override
    public CompletableFuture<?> asyncExecuteCallable(int taskExecutorHash, Callable<?> callable) {
        return asyncExecuteCallable(RandomUtils.randomInt(), taskExecutorHash, callable);
    }

    @Override
    public void shutdown() {
        for (ExecutorService executor : executors) {
            ThreadUtils.shutdown(executor);
        }
    }

    /**
     * 请求成功过后依然在相同的线程执行回调任务
     */
    public Executor currentThreadExecutor() {

        var threadId = Thread.currentThread().getId();
        ExecutorService executorService = threadId2ExecutorMap.get(threadId);
        if (executorService != null) {
            return executorService;
        }
        return executors[calTaskExecutorHash(RandomUtils.randomInt())];
    }


    public Map<Long, ExecutorService> getExecutorService() {
        return threadId2ExecutorMap;
    }
}
