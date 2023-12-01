package com.awake.storage.strategy;

import com.awake.util.JsonUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @version : 1.0
 * @ClassName: JsonToListConverter
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:13
 **/
public class JsonToListConverter implements ConditionalGenericConverter {


    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType() == String.class && List.class.isAssignableFrom(targetType.getType());
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, List.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        // String content = (String) source;
        // return targetType.isPrimitive() ? JsonUtil.string2Object(content, targetType.getObjectType())
        //         : JsonUtil.string2Array(content, targetType.getType());
        Class<?> clazz = null;

        String content = (String) source;
        for (var v : targetType.getResolvableType().getGenerics()){
            clazz = (Class<?>) v.getType();
        }
        return Collections.unmodifiableList(JsonUtils.string2List(content, clazz));
    }
}
