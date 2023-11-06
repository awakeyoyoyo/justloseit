package com.awake.scheduler.enhance;

import com.awake.util.ReflectionUtils;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @version : 1.0
 * @ClassName: ReflectScheduler
 * @Description: 动态代理被Scheduler注解标注的方法，为了避免反射最终会用javassist字节码增强的方法去代理ReflectScheduler
 * @Auther: awake
 * @Date: 2023/11/6 10:33
 **/
@Data
public class ReflectScheduler implements IScheduler{
    private Object bean;

    private Method method;

    public static ReflectScheduler valueOf(Object bean, Method method) {
        var scheduler = new ReflectScheduler();
        scheduler.bean = bean;
        scheduler.method = method;
        return scheduler;
    }

    @Override
    public void invoke() {
        ReflectionUtils.invokeMethod(bean, method);
    }
}
