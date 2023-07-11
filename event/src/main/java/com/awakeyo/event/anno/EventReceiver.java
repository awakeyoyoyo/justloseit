package com.awakeyo.event.anno;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: EventReceiver
 * @Description: 接收事件的注解
 * @Auther: awake
 * @Date: 2022/9/8 16:08
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventReceiver {
    /**
     * 是否异步
     * @return
     */
    boolean async() default false;
}
