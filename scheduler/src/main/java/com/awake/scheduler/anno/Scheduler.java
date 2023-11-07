package com.awake.scheduler.anno;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: Scheduler
 * @Description: time task scheduling based on cron expression
 * @Auther: awake
 * @Date: 2023/11/6 10:22
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Scheduler {

    String cron();
}
