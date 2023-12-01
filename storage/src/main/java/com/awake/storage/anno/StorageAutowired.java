package com.awake.storage.anno;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: StorageAutowired
 * @Description: 静态数据的注入
 * @Auther: awake
 * @Date: 2023/12/1 10:37
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface StorageAutowired {

    String value() default "";

    boolean unique() default false;
}
