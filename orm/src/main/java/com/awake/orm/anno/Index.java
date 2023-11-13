package com.awake.orm.anno;

import java.lang.annotation.*;

/**
 * @author awakeyoyoyo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Index {
    boolean ascending();

    boolean unique();

    // 默认小于0不开启TTL文档超时索引
    long ttlExpireAfterSeconds() default -1L;
}
