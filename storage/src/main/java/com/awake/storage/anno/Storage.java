package com.awake.storage.anno;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: Storage
 * @Description: 可以指定对应的资源文件名（只指定文件名，不需要文件后缀），如果不指定资源文件名，则默认通过扫描路径获取与类名相同的文件资源
 * @Auther: awake
 * @Date: 2023/12/1 10:36
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Storage {
    String value() default "";
}
