package com.awake.orm.model;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * @version : 1.0
 * @ClassName: IndexDef
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:10
 **/
@Data
public class IndexDef {

    private Field field;
    private boolean ascending;
    private boolean unique;
    private long ttlExpireAfterSeconds;

    public IndexDef(Field field, boolean ascending, boolean unique, long ttlExpireAfterSeconds) {
        this.field = field;
        this.ascending = ascending;
        this.unique = unique;
        this.ttlExpireAfterSeconds = ttlExpireAfterSeconds;
    }
}
