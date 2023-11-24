package com.awake.orm.accessor;

import com.awake.orm.OrmContext;
import com.awake.orm.autoconfigure.OrmAutoConfiguration;
import com.awake.orm.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @version : 1.0
 * @ClassName: AccessorTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/24 16:49
 **/
@SpringBootTest(classes = {OrmAutoConfiguration.class})
public class AccessorTest {

    @Test
    public void insert() {

        var userEntity = new UserEntity(1, (byte) 2, (short) 3, 5, true, "orm", "orm");
        OrmContext.getAccessor().insert(userEntity);
    }


    @Test
    public void delete() {

        OrmContext.getAccessor().delete(1L, UserEntity.class);
    }


    @Test
    public void update() {

        var userEntity = new UserEntity(1, (byte) 2, (short) 3, 5, true, "update", "update");
        OrmContext.getAccessor().update(userEntity);
    }

    @Test
    public void load() {

        var ent = (UserEntity) OrmContext.getAccessor().load(1L, UserEntity.class);
        System.out.println(ent);
    }


    // 批量插入
    @Test
    public void batchInsert() {

        var listUser = new ArrayList<UserEntity>();
        for (var i = 2; i <= 10; i++) {
            var userEntity = new UserEntity(i, (byte) 1, (short) i, i, true, "helloOrm" + i, "helloOrm" + i);
            listUser.add(userEntity);
        }
        OrmContext.getAccessor().batchInsert(listUser);
    }

    // 批量更新
    @Test
    public void batchUpdate() {
        var userEntity = new UserEntity(1, (byte) 2, (short) 3, 5, true, "helloBatchUpdate", "helloOrm");
        userEntity.setC(222);
        OrmContext.getAccessor().batchUpdate(Collections.singletonList(userEntity));
    }
}
