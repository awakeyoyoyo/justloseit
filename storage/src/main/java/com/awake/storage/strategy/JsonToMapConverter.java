package com.awake.storage.strategy;

import com.awake.util.JsonUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @version : 1.0
 * @ClassName: JsonToMapConverter
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:13
 **/
public class JsonToMapConverter implements ConditionalGenericConverter {
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType() == String.class && Map.class.isAssignableFrom(targetType.getType());
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Map.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        String content = (String) source;
        return JsonUtils.string2Map(content, targetType.getMapKeyTypeDescriptor().getType(), targetType.getMapValueTypeDescriptor().getType());
//        return JsonUtils.string2Object(content, targetType.getType());
        // return JsonUtil.string2Map(content, targetType.getMapKeyTypeDescriptor().getType()
        //         , targetType.getMapValueTypeDescriptor().getType());
    }
}
