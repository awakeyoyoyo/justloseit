package com.awake.storage.strategy;

import org.springframework.core.convert.converter.Converter;

/**
 * @version : 1.0
 * @ClassName: StringToDataConverter
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:15
 **/
public class StringToDateConverter implements Converter<String,Class<?>> {

    @Override
    public Class<?> convert(String source) {
        return null;
    }
}
