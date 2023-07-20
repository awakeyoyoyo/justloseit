package com.awake.thread.anno;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version : 1.0
 * @ClassName: SafeRunnable
 * @Description: Runnable 加日志打印
 * @Auther: awake
 * @Date: 2023/3/8 11:15
 **/
public class SafeRunnable implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(SafeRunnable.class);

    private Runnable runnable;

    private SafeRunnable() {
    }

    public static SafeRunnable valueOf(Runnable runnable) {
        SafeRunnable run = new SafeRunnable();
        run.runnable = runnable;
        return run;
    }

    @Override
    public void run() {
        try {
            runnable.run();
        } catch (Exception e) {
            logger.error("unknown exception", e);
        } catch (Throwable t) {
            logger.error("unknown error", t);
        }
    }

}
