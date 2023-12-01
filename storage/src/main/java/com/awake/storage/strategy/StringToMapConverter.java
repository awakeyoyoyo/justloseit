package com.awake.storage.strategy;


import com.awake.util.JsonUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: StringToMapConverter
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:16
 **/
public class StringToMapConverter implements Converter<String, Map<String, Object>> {
    @Override
    public Map<String, Object> convert(String source) {
        return JsonUtils.string2Map(source, String.class, Object.class);
    }
}
