package com.awake.consistent;

import com.awake.net.util.FastTreeMapIntLong;
import org.junit.Assert;
import org.junit.Test;

import java.util.TreeMap;

/**
 * @version : 1.0
 * @ClassName: FastTreeMapIntLongTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/1 10:13
 **/
public class FastTreeMapIntLongTest {

    @Test
    public void test() {
        var treeMap = new TreeMap<Integer, Long>();
        //只存在偶数
        for (var i = 0; i < 100; i = i + 2) {
            treeMap.put(i, (long) i);
        }

        var fastTreeMap = new FastTreeMapIntLong(treeMap);
        Assert.assertEquals(fastTreeMap.get(0), 0);
        Assert.assertEquals(fastTreeMap.get(8), 8);
        Assert.assertEquals(fastTreeMap.get(98), 98);
        Assert.assertFalse(fastTreeMap.contains(-1));
        Assert.assertFalse(fastTreeMap.contains(1));
        Assert.assertFalse(fastTreeMap.contains(100));
        Assert.assertTrue(fastTreeMap.contains(44));
        Assert.assertEquals(fastTreeMap.getValueByCeilingKey(-1), 0);
        Assert.assertEquals(fastTreeMap.getValueByCeilingKey(-100), 0);
        Assert.assertEquals(fastTreeMap.getValueByCeilingKey(0), 0);
        Assert.assertEquals(fastTreeMap.getValueByCeilingKey(1), 2);
        Assert.assertEquals(fastTreeMap.getValueByCeilingKey(45), 46);
        Assert.assertEquals(fastTreeMap.getValueByCeilingKey(97), 98);
        Assert.assertEquals(fastTreeMap.getValueByCeilingKey(100), 0);
        Assert.assertEquals(fastTreeMap.indexOfNearestCeilingKey(-1), 0);
        Assert.assertEquals(fastTreeMap.indexOfNearestCeilingKey(0), 0);
        Assert.assertEquals(fastTreeMap.indexOfNearestCeilingKey(1), 1);
    }
}
