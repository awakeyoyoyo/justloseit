package com.awake.orm.cache;

import com.awake.orm.OrmContext;
import com.awake.orm.autoconfigure.OrmAutoTestConfiguration;
import com.awake.orm.entity.UserEntity;
import com.awake.util.base.ThreadUtils;
import com.awake.util.time.TimeUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

/**
 * @version : 1.0
 * @ClassName: OrmTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 11:13
 **/
@SpringBootTest(classes = {OrmAutoTestConfiguration.class})
public class OrmTest {

    private static final Logger logger = LoggerFactory.getLogger(OrmTest.class);
    @Autowired
    private UserManager userManager;
    @Test
    public void test() {
        var collection = OrmContext.getOrmManager().getCollection(UserEntity.class);
        collection.drop();

        batchInsert();
        // 通过注解自动注入的方式去拿到UserEntity的EntityCaches
        var userEntityCaches = userManager.getUserEntityCaches();
        for (int i = 1; i <= 10; i++) {
            var entity = userEntityCaches.load((long) i);
            entity.setE("update" + i);
            entity.setC(i);
            userEntityCaches.update(entity);
        }
        for (int i = 1; i <= 10; i++) {
            var entity = userEntityCaches.load((long) i);
            logger.info("[userEntity]:{}",entity.toString());
        }
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    @Test
    public void TestLoadOrCreate() {
        var collection = OrmContext.getOrmManager().getCollection(UserEntity.class);
        collection.drop();
        // 动态去拿到UserEntity的EntityCaches
        @SuppressWarnings("unchecked")
        var userEntityCaches = userManager.getUserEntityCaches();
        for (int i = 1; i <= 10; i++) {
            var entity = userEntityCaches.loadOrCreate((long) i);
            entity.setE("LoadOrInsert" + i);
            entity.setC(i);
            userEntityCaches.update(entity);
        }
        for (int i = 1; i <= 10; i++) {
            var entity = userEntityCaches.load((long) i);
            logger.info("[userEntity]:{}",entity.toString());
        }
        ThreadUtils.sleep(60 * TimeUtils.MILLIS_PER_SECOND);
    }


    public void batchInsert() {
        var listUser = new ArrayList<UserEntity>();
        for (var i = 1; i <= 10; i++) {
            var userEntity = new UserEntity(i, (byte) 1, (short) i, i, true, "helloOrm" + i, "helloOrm" + i);
            listUser.add(userEntity);
        }
        OrmContext.getAccessor().batchInsert(listUser);
    }
}
