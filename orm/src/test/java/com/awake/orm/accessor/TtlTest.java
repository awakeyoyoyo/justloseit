package com.awake.orm.accessor;

import com.awake.orm.OrmContext;
import com.awake.orm.autoconfigure.OrmAutoConfiguration;
import com.awake.orm.entity.MailEntity;
import com.awake.util.time.TimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @version : 1.0
 * @ClassName: TtlTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 16:40
 **/
@SpringBootTest(classes = {OrmAutoConfiguration.class})
public class TtlTest {
    @Test
    public void ttlTest() {
        var mailEntity = MailEntity.valueOf("a", "awake!", "hello ttl", new Date(TimeUtils.now()));
        OrmContext.getAccessor().insert(mailEntity);
    }

    @Test
    public void ttlTestLoad() {
        var ent = (MailEntity) OrmContext.getAccessor().load("a", MailEntity.class);
        System.out.println(ent);
    }
}
