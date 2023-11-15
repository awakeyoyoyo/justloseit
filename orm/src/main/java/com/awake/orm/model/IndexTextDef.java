package com.awake.orm.model;

import com.awake.orm.anno.IndexText;
import com.awake.util.AssertionUtils;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.StringUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @version : 1.0
 * @ClassName: IndexTextDef
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:10
 **/
@Data
public class IndexTextDef {

    private Field field;

    public IndexTextDef(Field field, IndexText indexText) {
        AssertionUtils.notNull(field);

        // 是否被private修饰
        if (!Modifier.isPrivate(field.getModifiers())) {
            throw new IllegalArgumentException(StringUtils.format("[{}]没有被private修饰", field.getName()));
        }

        // 唯一索引不能有set方法，为了避免客户端改变javabean中的属性
        Class<?> clazz = field.getDeclaringClass();
        AssertionUtils.notNull(clazz);
        this.field = field;
        ReflectionUtils.makeAccessible(field);
    }
}
