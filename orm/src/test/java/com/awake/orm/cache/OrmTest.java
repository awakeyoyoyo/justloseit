package com.awake.orm.cache;

import com.awake.orm.OrmContext;
import com.awake.orm.autoconfigure.OrmAutoTestConfiguration;
import com.awake.util.base.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: OrmTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 11:13
 **/
@SpringBootTest(classes = {OrmAutoTestConfiguration.class})
public class OrmTest {
    @Autowired
    private UserManager userManager;
    @Test
    public void test() {
        // 通过注解自动注入的方式去拿到UserEntity的EntityCaches
        var userEntityCaches = userManager.getUserEntityCaches();
//
        for (int i = 1; i <= 10; i++) {
            var entity = userEntityCaches.load((long) i);
            entity.setE("update" + i);
            entity.setC(i);
            userEntityCaches.update(entity);
        }

        OrmContext.getOrmManager().getAllEntityCaches().forEach(it -> System.out.println(it.recordStatus()));

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
