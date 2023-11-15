package com.awake.orm.model;

import com.awake.orm.cache.persister.PersisterStrategy;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: EntityDef
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:09
 **/
@Data
public class EntityDef {

    private Field idField;

    private Class<? extends IEntity<?>> clazz;

    private int cacheSize;

    private long expireMillisecond;

    private PersisterStrategy persisterStrategy;

    private Map<String, IndexDef> indexDefMap;

    private Map<String, IndexTextDef> indexTextDefMap;

    public static EntityDef valueOf(Field idField, Class<? extends IEntity<?>> clazz, int cacheSize, long expireMillisecond
            , PersisterStrategy persisterStrategy, Map<String, IndexDef> indexDefMap, Map<String, IndexTextDef> indexTextDefMap) {
        var entityDef = new EntityDef();
        entityDef.idField = idField;
        entityDef.clazz = clazz;
        entityDef.cacheSize = cacheSize;
        entityDef.expireMillisecond = expireMillisecond;
        entityDef.persisterStrategy = persisterStrategy;
        entityDef.indexDefMap = indexDefMap;
        entityDef.indexTextDefMap = indexTextDefMap;
        return entityDef;
    }


}
