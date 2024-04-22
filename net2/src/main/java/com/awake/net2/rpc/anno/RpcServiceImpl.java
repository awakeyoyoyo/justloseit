package com.awake.net2.rpc.anno;

import java.lang.annotation.*;

/**
 * Rpc服务实现
 * @Author：lqh
 * @Date：2024/3/29 14:46
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RpcServiceImpl {
    String serviceName() default "";
}
