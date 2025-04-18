package com.awake.rpc.anno;

import java.lang.annotation.*;

/**
 * Rpc服务注册
 * @Author：lqh
 * @Date：2024/3/29 14:51
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RpcService {

    int moduleId() default 0;
}
