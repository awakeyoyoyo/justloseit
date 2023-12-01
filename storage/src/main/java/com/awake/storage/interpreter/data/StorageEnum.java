package com.awake.storage.interpreter.data;

import com.awake.util.AssertionUtils;
import com.awake.util.base.StringUtils;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: StorageEnum
 * @Description: 支持读取配置文件的类型后缀
 * @Auther: awake
 * @Date: 2023/12/1 10:40
 **/
public enum  StorageEnum{
    //
    EXCEL_XLS("xls"),

    EXCEL_XLSX("xlsx"),

    JSON("json"),

    CSV("csv"),

            ;

    private static Map<String, StorageEnum> typeMap = new HashMap<>();

    static {
        for (var resourceEnum : StorageEnum.values()) {
            var previousValue = typeMap.putIfAbsent(resourceEnum.type, resourceEnum);
            AssertionUtils.isNull(previousValue, "ResourceEnum should not contain enumeration classes [{}] and [{}] of repeated type", resourceEnum, previousValue);
        }
    }

    private String type;

    StorageEnum(String type) {
        this.type = type;
    }

    @Nullable
    public static StorageEnum getResourceEnumByType(String type) {
        return typeMap.get(type);
    }

    public static boolean containsResourceEnum(String type) {
        return typeMap.containsKey(type);
    }

    public static boolean isExcel(String type) {
        var resourceEnum = getResourceEnumByType(type);
        return resourceEnum == StorageEnum.EXCEL_XLS || resourceEnum == StorageEnum.EXCEL_XLSX;
    }

    public static String typesToString() {
        return StringUtils.joinWith(StringUtils.SPACE, typeMap.values().toArray());
    }

    public String getType() {
        return type;
    }
}
