package com.awake.orm.accessor;

import com.awake.orm.autoconfigure.OrmAutoTestConfiguration;
import com.awake.orm.entity.UserEntity;
import com.awake.orm.entity.bag.Item;
import com.awake.orm.entity.bag.ItemBag;
import com.awake.orm.entity.bag.MapEntity;
import com.awake.orm.OrmContext;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: AccessorTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/24 16:49
 **/
@SpringBootTest(classes = {OrmAutoTestConfiguration.class})
public class AccessorTest {

    @Test
    public void insert() {

        var userEntity = new UserEntity(1, (byte) 2, (short) 3, 1, true, "orm", "orm");
        OrmContext.getAccessor().insert(userEntity);
    }


    @Test
    public void delete() {

        OrmContext.getAccessor().delete(1L, UserEntity.class);
    }


    @Test
    public void update() {

        var userEntity = new UserEntity(1, (byte) 2, (short) 3, 1, true, "update", "update");
        OrmContext.getAccessor().update(userEntity);
    }

    @Test
    public void load() {

        var ent = (UserEntity) OrmContext.getAccessor().load(2L, UserEntity.class);
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
        var userEntity = new UserEntity(3, (byte) 2, (short) 3, 5, true, "helloBatchUpdate", "helloOrm");
        userEntity.setC(222);
        OrmContext.getAccessor().batchUpdate(Collections.singletonList(userEntity));
    }

    // 插入map数据
    @Test
    public void insertMapData() {
        OrmContext.getAccessor().delete(11, MapEntity.class);

        var entity = new MapEntity();
        entity.setId(11L);

        var bagMap = new HashMap<String, ItemBag>();
        entity.setBagMap(bagMap);

        var itemMap = new HashMap<Long, Item>();
        itemMap.put(1L, new Item(1, "item1"));
        itemMap.put(2L, new Item(2, "item1"));
        itemMap.put(3L, new Item(3, "item1"));

        var bagItem1 = new ItemBag(1, "desc1", itemMap);
        var bagItem2 = new ItemBag(2, "desc2", itemMap);
        var bagItem3 = new ItemBag(3, "desc3", itemMap);

        bagMap.put("bag1", bagItem1);
        bagMap.put("bag2", bagItem2);
        bagMap.put("bag3", bagItem3);

        var map = new HashMap<String, Map<String, String>>();
        map.put("a", Map.of("b", "b"));
        entity.setBaseMap(map);

        OrmContext.getAccessor().insert(entity);

        var myEntity = OrmContext.getAccessor().load(11, MapEntity.class);
        Assert.assertEquals(entity, myEntity);
    }
}
