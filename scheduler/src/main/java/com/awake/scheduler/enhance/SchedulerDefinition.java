package com.awake.scheduler.enhance;

import com.awake.util.ReflectionUtils;
import com.awake.util.time.TimeUtils;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.Data;
import org.springframework.scheduling.support.CronExpression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @version : 1.0
 * @ClassName: SchedulerDefinition
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/6 10:30
 **/
@Data
public class SchedulerDefinition {

    private CronExpression cronExpression;

    private IScheduler scheduler;

    private long triggerTimestamp;

    public static SchedulerDefinition valueOf(String cron, Object bean, Method method) throws NoSuchMethodException, IllegalAccessException, InstantiationException, CannotCompileException, NotFoundException, InvocationTargetException {
        var schedulerDef = new SchedulerDefinition();
        var cronExpression = CronExpression.parse(cron);
        schedulerDef.cronExpression = cronExpression;

        // bytecode enhancements to avoid reflection
        schedulerDef.scheduler = EnhanceUtils.createScheduler(ReflectScheduler.valueOf(bean, method));

        schedulerDef.triggerTimestamp = TimeUtils.nextTimestampByCronExpression(cronExpression, TimeUtils.currentTimeMillis());
        ReflectionUtils.makeAccessible(method);
        return schedulerDef;
    }

    public static SchedulerDefinition valueOf(String cron, Runnable runnable) {
        var schedulerDef = new SchedulerDefinition();
        var cronExpression = CronExpression.parse(cron);
        schedulerDef.cronExpression = cronExpression;
        schedulerDef.scheduler = SchedulerRunnable.valueOf(runnable);
        schedulerDef.triggerTimestamp = TimeUtils.nextTimestampByCronExpression(cronExpression, TimeUtils.currentTimeMillis());
        return schedulerDef;
    }
}
