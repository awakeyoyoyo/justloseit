package com.awake.orm.anno;

import java.lang.annotation.*;

/**
 * @author awakeyoyoyo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Cache {

    String value() default "default";
}
