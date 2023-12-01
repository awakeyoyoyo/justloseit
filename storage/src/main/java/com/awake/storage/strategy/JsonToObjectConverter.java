package com.awake.storage.strategy;

import com.awake.util.JsonUtils;
import com.awake.util.ReflectionUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @version : 1.0
 * @ClassName: JsonToObjectConverter
 * @Description: 转换一个String到一个POJO对象，且这个对象不能继承如何接口
 * @Auther: awake
 * @Date: 2023/12/1 11:13
 **/
public class JsonToObjectConverter implements ConditionalGenericConverter {


    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType() != String.class) {
            return false;
        }

        if (targetType.getType().isPrimitive()) {
            return false;
        }

        if (Number.class.isAssignableFrom(targetType.getType())) {
            return false;
        }

        if (CharSequence.class.isAssignableFrom(targetType.getType())) {
            return false;
        }

        return ReflectionUtils.isPojoClass(targetType.getType());
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Object.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        String content = (String) source;
        return JsonUtils.string2Object(content, targetType.getType());
    }
}
