package com.awake.orm.text;

import com.awake.orm.OrmContext;
import com.awake.orm.autoconfigure.OrmAutoTestConfiguration;
import com.awake.orm.entity.UserEntity;
import com.awake.util.base.ThreadUtils;
import com.mongodb.client.model.Filters;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: IndexTextTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 16:47
 **/
@SpringBootTest(classes = {OrmAutoTestConfiguration.class})
public class IndexTextTest {


    @Test
    public void insertTest() {

        var listUser = new ArrayList<UserEntity>();
        var userEntity = new UserEntity(1, (byte) 1, (short) 1, 1, true, "我哩二十几年嚟，乜都冇做过阿。我唔想临走熄灯个阵，连一件值得记得的事都冇", null);
        listUser.add(userEntity);
        userEntity = new UserEntity(2, (byte) 2, (short) 2, 2, true, "我仲有野输乜？唔紧要啦，赢又好，输又好，怯，你就输成世，乜都翻唔到转头了。", null);
        listUser.add(userEntity);
        userEntity = new UserEntity(3, (byte) 3, (short) 3, 3, true, "记住自己哩次点解要上台，上得去，就唔好怯。怯，你就输成世。", null);
        listUser.add(userEntity);
        OrmContext.getAccessor().batchInsert(listUser);
//        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    @Test
    public void queryTest() {


        var collection = OrmContext.getOrmManager().getCollection(UserEntity.class);
        collection.find(Filters.text("怯，你就输成世")).forEach(new Consumer<UserEntity>() {
            @Override
            public void accept(UserEntity userEntity) {
                System.out.println(userEntity);
            }
        });
        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}
