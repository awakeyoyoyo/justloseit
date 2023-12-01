package com.awake.storage.strategy;

import org.springframework.core.convert.converter.Converter;

/**
 * @version : 1.0
 * @ClassName: JsonToObjectConverter
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:13
 **/
public class JsonToObjectConverter implements Converter<String,Class<?>> {

    @Override
    public Class<?> convert(String source) {
        return null;
    }
}
