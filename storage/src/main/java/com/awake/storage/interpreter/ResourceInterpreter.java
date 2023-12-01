package com.awake.storage.interpreter;

import com.awake.exception.RunException;
import com.awake.storage.anno.AliasFieldName;
import com.awake.storage.anno.Id;
import com.awake.storage.interpreter.data.StorageData;
import com.awake.storage.interpreter.data.StorageEnum;
import com.awake.storage.strategy.*;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.StringUtils;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.TypeDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version : 1.0
 * @ClassName: ResourceInterpreter
 * @Description: 资源解释器
 * @Auther: awake
 * @Date: 2023/12/1 11:06
 **/
public class ResourceInterpreter {

    private static final TypeDescriptor TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);

    private static final ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();

    static {
        var converters = new HashSet<>();
        converters.add(new JsonToArrayConverter());
        converters.add(new JsonToListConverter());
        converters.add(new JsonToMapConverter());
        converters.add(new JsonToObjectConverter());
        converters.add(new StringToClassConverter());
        converters.add(new StringToDateConverter());
        converters.add(new StringToMapConverter());
        conversionServiceFactoryBean.setConverters(converters);
        conversionServiceFactoryBean.afterPropertiesSet();
    }

    public static <T> List<T> read(InputStream inputStream, Class<T> clazz, String suffix) throws IOException {
        StorageData resource = null;
        var resourceEnum = StorageEnum.getResourceEnumByType(suffix);
        if (resourceEnum == StorageEnum.JSON) {
            resource = JsonReader.readResourceDataFromJson(inputStream);
        } else if (resourceEnum == StorageEnum.EXCEL_XLS || resourceEnum == StorageEnum.EXCEL_XLSX) {
            resource = ExcelReader.readResourceDataFromExcel(inputStream, clazz.getSimpleName());
        } else if (resourceEnum == StorageEnum.CSV) {
            resource = CsvReader.readResourceDataFromCSV(inputStream, clazz.getSimpleName());
        } else {
            throw new RunException("Configuration type [{}] of file [{}] is not supported", suffix, clazz.getSimpleName());
        }

        var result = new ArrayList<T>();
        //获取所有字段
        var cellFieldMap = getCellFieldMap(resource, clazz);
        var fieldInfos = getFieldInfos(cellFieldMap, clazz);


        var iterator = resource.getRows().iterator();
        // 从ROW_SERVER这行开始读取数据
        while (iterator.hasNext()) {
            var columns = iterator.next();
            var instance = ReflectionUtils.newInstance(clazz);

            for (var fieldInfo : fieldInfos) {
                var content = columns.get(fieldInfo.index);
                if (StringUtils.isNotEmpty(content) || fieldInfo.field.getType() == String.class) {
                    inject(instance, fieldInfo.field, content);
                }
            }
            result.add(instance);
        }
        return result;
    }

    private static void inject(Object instance, Field field, String content) {
        try {
            var targetType = new TypeDescriptor(field);
            var value = conversionServiceFactoryBean.getObject().convert(content, TYPE_DESCRIPTOR, targetType);
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, instance, value);
        } catch (Exception e) {
            throw new RunException("Unable to convert [content:{}] to property [field:{}] in Excel resource [class:{}]", content, field.getName(), instance.getClass().getSimpleName(), e);
        }
    }

    // 优先使用ExcelFieldName注解表示的值当作列名
    private static String getExcelFieldName(Field field) {
        return field.isAnnotationPresent(AliasFieldName.class) ? field.getAnnotation(AliasFieldName.class).value() : field.getName();
    }

    // 只读取代码里写的字段
    private static Collection<FieldInfo> getFieldInfos(Map<String, Integer> cellFieldMap, Class<?> clazz) {
        var fieldList = ReflectionUtils.notStaticAndTransientFields(clazz);
        // 检测field的合法性，field必须可以在excel中找到对应的列，有找不到的列在启动时候就发现
        for (var field : fieldList) {
            var fieldName = getExcelFieldName(field);
            if (!cellFieldMap.containsKey(fieldName)) {
                throw new RunException("The declaration attribute [filed:{}] of the resource class [class:{}] cannot be obtained, please check the format of the configuration table", fieldName, clazz);
            }

            if (field.isAnnotationPresent(Id.class)) {
                var cellIndex = cellFieldMap.get(fieldName);
                if (cellIndex != 0) {
                    throw new RunException("The primary key [Id:{}] of the resource class [class:{}] must be placed in the first column of the Excel configuration table, please check the format of the configuration table", fieldName, clazz);
                }
            }
        }
        return fieldList.stream().map(it -> new FieldInfo(cellFieldMap.get(getExcelFieldName(it)), it)).collect(Collectors.toList());
    }

    private static class FieldInfo {
        public final int index;
        public final Field field;

        public FieldInfo(int index, Field field) {
            this.index = index;
            this.field = field;
        }
    }

    public static Map<String, Integer> getCellFieldMap(StorageData resource, Class<?> clazz) {
        var header = resource.getHeaders();
        if (header == null) {
            throw new RunException("Failed to get attribute control column from excel file of resource [class:{}]", clazz.getSimpleName());
        }

        var cellFieldMap = new HashMap<String, Integer>();
        for (var i = 0; i < header.size(); i++) {
            var cell = header.get(i);
            if (Objects.isNull(cell)) {
                continue;
            }

            var name = cell.getName();
            if (StringUtils.isEmpty(name)) {
                continue;
            }
            var previousValue = cellFieldMap.put(name, cell.getIndex());
            if (Objects.nonNull(previousValue)) {
                throw new RunException("There are duplicate attribute control columns [field:{}] in the Excel file of the resource [class:{}]", name, clazz.getSimpleName());
            }
        }
        return cellFieldMap;
    }
}
