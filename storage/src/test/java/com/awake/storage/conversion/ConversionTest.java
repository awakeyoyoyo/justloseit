package com.awake.storage.conversion;

import com.awake.storage.strategy.JsonToArrayConverter;
import com.awake.storage.strategy.JsonToMapConverter;
import com.awake.storage.strategy.StringToClassConverter;
import com.awake.storage.strategy.StringToDateConverter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.util.*;

/**
 * @version : 1.0
 * @ClassName: ConversionTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 15:26
 **/
public class ConversionTest {

    private static final ConversionServiceFactoryBean csfb = new ConversionServiceFactoryBean();
    private static final Set<Object> converters = new HashSet<>();

    private static final StringToDateConverter std = new StringToDateConverter();
    private static final StringToClassConverter stcc = new StringToClassConverter();
    private static final JsonToMapConverter jtmc = new JsonToMapConverter();
    private static final JsonToArrayConverter jtac = new JsonToArrayConverter();

    static {
        converters.add(std);
        converters.add(stcc);
        converters.add(jtmc);
        converters.add(jtac);
        csfb.setConverters(converters);
        csfb.afterPropertiesSet();
    }


    @Test
    public void string2Integer() {
        ConversionService conversionService = csfb.getObject();
        Integer result = conversionService.convert("123", Integer.class);
        System.out.println(result);
        Assert.assertEquals(123, result.intValue());
    }

    @Test
    public void string2Map() {
        ConversionService conversionService = csfb.getObject();
        //Json to Map
        String str = "{\"1\":1,\"2\":2,\"3\":3}";
        // 注意：第3个参数不能写成TypeDescriptor.valueOf(map.getClass())  而是要明确指定Map的key和value的类型
        @SuppressWarnings("unchecked")
        var map = (Map<Integer, Integer>) conversionService.convert
                (str, TypeDescriptor.valueOf(String.class),
                        TypeDescriptor.map(HashMap.class,
                                TypeDescriptor.valueOf(String.class),
                                TypeDescriptor.valueOf(Integer.class)));
        System.out.println(map);
        Assert.assertEquals(3, map.size());
    }

    @Test
    public void string2Array() {
        ConversionService conversionService = csfb.getObject();
        String str = "[1,2,3]";

        Integer[] array = (Integer[]) conversionService.convert(str, TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(Integer[].class));
        System.out.println(Arrays.toString(array));
        Assert.assertEquals(3, array.length);
    }
}
