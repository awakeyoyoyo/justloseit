package com.awake.storage.anno;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: Index
 * @Description: 索引的名称使用字段属性的名称，用HaspMap实现
 * @Auther: awake
 * @Date: 2023/12/1 10:40
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Index {

    boolean unique() default false;

}