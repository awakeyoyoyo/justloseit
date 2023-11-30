package com.awake.orm.query;

import com.awake.orm.OrmContext;
import com.awake.orm.autoconfigure.OrmAutoTestConfiguration;
import com.awake.orm.entity.UserEntity;
import com.awake.orm.util.MongoIdUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: QueryTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 16:37
 **/
@SpringBootTest(classes = {OrmAutoTestConfiguration.class})
public class QueryTest {

    @Test
    public void queryAllTest() {

        var id = MongoIdUtils.getIncrementIdFromMongoDefault(UserEntity.class);
        var entity = new UserEntity();
        entity.setId(id);
        entity.setC((int)id);
        entity.setE("User_"+id);
        OrmContext.getAccessor().insert(entity);
        var list = OrmContext.getQuery(UserEntity.class).eq("e", "User_2").eq("f", null).queryAll();
        System.out.println(list);
    }

    @Test
    public void queryByFieldTest() {
        var list = OrmContext.getQuery(UserEntity.class).eq("a", 1).queryAll();
        System.out.println(list);
    }

}
