package com.awake.orm.anno;

import java.lang.annotation.*;

/**
 * Mongodb不支持中文分词
 * @author awakeyoyoyo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IndexText {
}
