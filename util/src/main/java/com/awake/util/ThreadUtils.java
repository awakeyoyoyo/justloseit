package com.awake.util;


import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @version : 1.0
 * @ClassName: ThreadUtils
 * @Description: 线程相关工具类
 * @Auther: awake
 * @Date: 2023/3/10 11:37
 **/
public class ThreadUtils {
    private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

    private static final int WAIT_TIME = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 获取当前线程的线程组
     */
    public static ThreadGroup currentThreadGroup() {
        SecurityManager s = System.getSecurityManager();
        return (null != s) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    public static void shutdown(ExecutorService executor) {
        try {
            if (!executor.isTerminated()) {

                executor.shutdown();

                if (!executor.awaitTermination(WAIT_TIME, TIME_UNIT)) {
                    executor.shutdownNow();
                }

            }
        } catch (Exception e) {
            logger.error("[{}] is failed to shutdown! ", executor, e);
        }
    }

    public static void shutdownForkJoinPool() {
        try {
            ForkJoinPool.commonPool().shutdown();

            if (ForkJoinPool.commonPool().awaitTermination(WAIT_TIME, TimeUnit.SECONDS)) {
                ForkJoinPool.commonPool().shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void shutdownEventLoopGracefully(String executorGroupName, EventExecutorGroup executor) {
        if (executor == null) {
            return;
        }
        try {
            if (!executor.isTerminated()) {
                executor.shutdownGracefully();
            }
        } catch (Exception e) {
            logger.error("[{}] is failed to shutdown! ", executorGroupName, e);
            return;
        }
        logger.info("[{}] shutdown gracefully.", executorGroupName);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
