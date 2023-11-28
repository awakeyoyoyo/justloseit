package com.awake.orm.cache;

import com.awake.orm.autoconfigure.OrmAutoTestConfiguration;
import com.awake.orm.entity.UserEntity;
import com.awake.orm.OrmContext;
import com.awake.util.base.ThreadUtils;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: EntityCachesTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 17:31
 **/

@SpringBootTest(classes = {OrmAutoTestConfiguration.class})
public class EntityCachesTest {

    @Autowired
    private UserManager userManager;

    @Test
    public void test() {

        // 动态去拿到UserEntity的EntityCaches
        @SuppressWarnings("unchecked")
        var userEntityCaches = (IEntityCache<Long, UserEntity>) OrmContext.getOrmManager().getEntityCaches(UserEntity.class);

        for (var i = 1; i <= 10; i++) {
            var entity = userEntityCaches.load((long) i);
            entity.setE("update" + i);
            entity.setC(i);
            userEntityCaches.update(entity);
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }


    @Test
    public void collectionTest() {
        var collection = OrmContext.getOrmManager().getCollection(UserEntity.class);
        var result = collection.updateOne(Filters.eq("_id", 1), new Document("$inc", new Document("p", 1L)));
        System.out.println(result);
        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
