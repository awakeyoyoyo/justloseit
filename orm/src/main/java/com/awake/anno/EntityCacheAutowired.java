package com.awake.anno;

import java.lang.annotation.*;

/**
 * @author awakeyoyoyo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface EntityCacheAutowired {
}
