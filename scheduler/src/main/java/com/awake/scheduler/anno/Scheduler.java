package com.awake.scheduler.anno;

/**
 * @version : 1.0
 * @ClassName: Scheduler
 * @Description: time task scheduling based on cron expression
 * @Auther: awake
 * @Date: 2023/11/6 10:22
 **/
public @interface Scheduler {

    String cron();
}
