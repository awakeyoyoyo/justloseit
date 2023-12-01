package com.awake.storage.anno;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: AliasFieldName
 * @Description:  指定文件列名，不指定则默认列名与字段名一致
 * @Auther: awake
 * @Date: 2023/12/1 10:36
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AliasFieldName {
    String value();
}
