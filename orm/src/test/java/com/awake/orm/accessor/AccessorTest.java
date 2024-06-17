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
        long id=11;
        OrmContext.getAccessor().delete(id, MapEntity.class);

        var entity = new MapEntity();
        entity.setId(id);

        var bagMap = new HashMap<String, ItemBag>();
        entity.setBagMap(bagMap);

        var itemMap = new HashMap<Long, Item>();
        itemMap.put(1L, new Item(1, "item1"));
        itemMap.put(2L, new Item(2, "item2"));
        itemMap.put(3L, new Item(3, "item3"));

        var bagItem1 = new ItemBag(1, "desc1", itemMap);
        var bagItem2 = new ItemBag(2, "desc2", itemMap);
        var bagItem3 = new ItemBag(3, "desc3", itemMap);

        bagMap.put("bag1", bagItem1);
        bagMap.put("bag2", bagItem2);
        bagMap.put("bag3", bagItem3);
        bagMap.put("bag4", null);

        var map = new HashMap<String, Map<String, String>>();
        map.put("a", Map.of("b", "b"));
        entity.setBaseMap(map);

        var longStringHashMap = new HashMap<Long, String>();
        longStringHashMap.put(100L, "hello map1");
        longStringHashMap.put(101L, "hello map2");
        longStringHashMap.put(103L, "hello map3");
        longStringHashMap.put(104L, null);
        entity.setLongStringMap(longStringHashMap);

        var intStringHashMap = new HashMap<Integer, String>();
        intStringHashMap.put(100, "hello map1");
        intStringHashMap.put(102, "hello map2");
        intStringHashMap.put(103, "hello map3");
        intStringHashMap.put(104, null);
        entity.setIntStringMap(intStringHashMap);

        var intBaseMap = new HashMap<Integer, Map<Integer, String>>();
        intBaseMap.put(1, Map.of(1, "1"));
        intBaseMap.put(2, Map.of(2, "2"));
        intBaseMap.put(3, Map.of(3, "3"));
        intBaseMap.put(4, null);
        entity.setIntBaseMap(intBaseMap);

        var charBagMap = new HashMap<Character, ItemBag>();
        entity.setCharBagMap(charBagMap);
        charBagMap.put('a', bagItem1);
        charBagMap.put('b', bagItem2);
        charBagMap.put('d', null);

        var boolBagMap = new HashMap<Boolean, ItemBag>();
        entity.setBoolBagMap(boolBagMap);
        boolBagMap.put(true, bagItem1);
        boolBagMap.put(false, null);

        var byteBagMap = new HashMap<Byte, ItemBag>();
        entity.setByteBagMap(byteBagMap);
        byteBagMap.put((byte) 1, bagItem1);
        byteBagMap.put((byte) 2, bagItem2);
        byteBagMap.put((byte) 3, null);

        var shortBagMap = new HashMap<Short, ItemBag>();
        entity.setShortBagMap(shortBagMap);
        shortBagMap.put((short) 1, bagItem1);
        shortBagMap.put((short) 2, bagItem2);
        shortBagMap.put((short) 3, null);

        var intBagMap = new HashMap<Integer, ItemBag>();
        entity.setIntBagMap(intBagMap);
        intBagMap.put(1, bagItem1);
        intBagMap.put(2, bagItem2);
        intBagMap.put(3, null);

        var longBagMap = new HashMap<Long, ItemBag>();
        entity.setLongBagMap(longBagMap);
        longBagMap.put(1L, bagItem1);
        longBagMap.put(2L, bagItem2);
        longBagMap.put(3L, null);

        var floatBagMap = new HashMap<Float, ItemBag>();
        entity.setFloatBagMap(floatBagMap);
        floatBagMap.put(1F, bagItem1);
        floatBagMap.put(2F, bagItem2);
        floatBagMap.put(3F, null);

        var doubleBagMap = new HashMap<Double, ItemBag>();
        entity.setDoubleBagMap(doubleBagMap);
        doubleBagMap.put(1D, bagItem1);
        doubleBagMap.put(2D, bagItem2);
        doubleBagMap.put(3D, null);

        OrmContext.getAccessor().insert(entity);
        var myEntity = OrmContext.getAccessor().load(id, MapEntity.class);
        Assert.assertEquals(entity.getBagMap(), myEntity.getBagMap());
        Assert.assertEquals(entity.getBaseMap(), myEntity.getBaseMap());
        Assert.assertEquals(entity.getIntStringMap(), myEntity.getIntStringMap());
        Assert.assertEquals(entity.getIntBagMap(), myEntity.getIntBagMap());
        Assert.assertEquals(entity, myEntity);
    }
}
