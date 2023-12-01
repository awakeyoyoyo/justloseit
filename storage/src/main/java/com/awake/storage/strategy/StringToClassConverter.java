package com.awake.storage.strategy;

import com.awake.util.base.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;

/**
 * @version : 1.0
 * @ClassName: StringToClassConcerter
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:15
 **/
public class StringToClassConverter implements Converter<String,Class<?>> {

    @Override
    public Class<?> convert(String source) {
        if (!source.contains(".") && !source.startsWith("[")) {
            source = "java.lang." + source;
        }

        try {
            return Class.forName(source, true, ClassUtils.getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(StringUtils.format("Unable to convert string [{}] to Class object", source));
        }

    }
}
