package com.awake.storage.model;

import com.awake.exception.RunException;
import com.awake.storage.anno.Index;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.ArrayUtils;
import com.awake.util.base.StringUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: IndexDef
 * @Description: 简化索引的名称，使用字段的名称作为索引的名称
 * @Auther: awake
 * @Date: 2023/12/1 11:22
 **/
@Data
public class IndexDef {

    private boolean unique;
    private Field field;

    public IndexDef(Field field) {
        ReflectionUtils.makeAccessible(field);
        this.field = field;
        var index = field.getAnnotation(Index.class);
        this.unique = index.unique();
    }

    public static Map<String, IndexDef> createResourceIndexes(Class<?> clazz) {
        var fields = ReflectionUtils.getFieldsByAnnoInPOJOClass(clazz, Index.class);
        var indexes = new ArrayList<IndexDef>(fields.length);

        var ormIndexes = ReflectionUtils.getFieldsByAnnoNameInPOJOClass(clazz, "com.zfoo.orm.model.anno.Index");
        if (ArrayUtils.isNotEmpty(ormIndexes)) {
            throw new RunException("Only the Index annotation of Storage can be used, and the Index annotation of Orm cannot be used in Storage. In order to avoid unnecessary misunderstanding and enhance the robustness of the project, such use is prohibited");
        }

        for (var field : fields) {
            IndexDef indexDef = new IndexDef(field);
            indexes.add(indexDef);
        }

        var result = new HashMap<String, IndexDef>();
        for (var index : indexes) {
            var indexName = index.field.getName();
            if (result.put(indexName, index) != null) {
                throw new RuntimeException(StringUtils.format("The  index name[{}] of resource class [{}] is duplicated.", indexName, clazz.getName()));
            }
        }

        return result;
    }
}
