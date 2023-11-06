package com.awake.scheduler.enhance;

/**
 * @version : 1.0
 * @ClassName: RunnableScheduler
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/6 10:48
 **/
public class SchedulerRunnable implements IScheduler {

    private Runnable runnable;

    public static SchedulerRunnable valueOf(Runnable runnable) {
        var scheduler = new SchedulerRunnable();
        scheduler.runnable = runnable;
        return scheduler;
    }

    @Override
    public void invoke() {
        runnable.run();
    }
}
