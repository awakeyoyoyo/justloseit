package com.awake.orm.entity;

import com.awake.orm.anno.*;
import com.awake.orm.model.IEntity;
import lombok.Data;

import java.util.List;

/**
 * @version : 1.0
 * @ClassName: UserEntity
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/24 16:49
 **/
@EntityCache(cache = @Cache("default"), persister = @Persister("time30s"))
@Data
public class UserEntity implements IEntity<Long> {
    @Id
    private long id;

    private byte a;

    private short b;

    @Index(ascending = false, unique = true)
    private int c;

    private boolean d;

    @IndexText
    private String e;

    //    @IndexText
    private String f;

    @Index(ascending = false, unique = false)
    private List<Integer> l;

    public UserEntity() {
    }

    public UserEntity(long id, byte a, short b, int c, boolean d, String e, String f) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", e='" + e + '\'' +
                ", f='" + f + '\'' +
                ", l=" + l +
                '}';
    }
}
