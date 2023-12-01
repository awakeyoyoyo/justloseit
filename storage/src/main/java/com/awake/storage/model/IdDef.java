package com.awake.storage.model;

import com.awake.exception.RunException;
import com.awake.storage.anno.Id;
import com.awake.util.ReflectionUtils;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @version : 1.0
 * @ClassName: IdDef
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:21
 **/
@Data
public class IdDef {
    private Field field;

    public static IdDef valueOf(Class<?> clazz) {
        var fields = ReflectionUtils.getFieldsByAnnoInPOJOClass(clazz, Id.class);
        if (fields.length <= 0) {
            throw new RunException("There is no a primary key identified by the Id annotation in class[{}](if it has indeed been annotated by the Id annotation, be careful not to use the ORM Id annotation)", clazz.getName());
        }
        if (fields.length > 1) {
            throw new RunException("The primary key Id annotation of class [{}] is duplicated", clazz.getName());
        }
        if (fields[0] == null) {
            throw new RunException("Illegal Id resource mapping object:" + clazz.getName());
        }
        var idField = fields[0];
        ReflectionUtils.makeAccessible(idField);
        var idDef = new IdDef();
        idDef.setField(idField);
        return idDef;
    }

}
