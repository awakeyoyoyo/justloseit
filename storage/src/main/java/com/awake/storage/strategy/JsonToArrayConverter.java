package com.awake.storage.strategy;

import com.awake.util.JsonUtils;
import com.awake.util.base.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @version : 1.0
 * @ClassName: JsonToArrayConverter
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:12
 **/
public class JsonToArrayConverter implements ConditionalGenericConverter {


    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType() == String.class && targetType.getType().isArray();
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Object[].class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        // String content = (String) source;
        // return targetType.isPrimitive() ? JsonUtil.string2Object(content, targetType.getObjectType())
        //         : JsonUtil.string2Array(content, targetType.getType());
        Class<?> clazz = null;

        String content = (String) source;

        String targetClazzName = targetType.getObjectType().getName();
        if (targetClazzName.contains(StringUtils.LEFT_SQUARE_BRACKET) || targetClazzName.contains(StringUtils.SEMICOLON)) {
            String clazzPath = targetClazzName.substring(2, targetClazzName.length() - 1);
            try {
                clazz = Class.forName(clazzPath);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            clazz = targetType.getObjectType();
        }

        return JsonUtils.string2Array(content, clazz);
    }
}

