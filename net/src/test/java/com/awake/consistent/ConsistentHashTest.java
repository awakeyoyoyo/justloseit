package com.awake.consistent;

import com.awake.net.util.ConsistentHash;
import com.awake.net.util.Pair;
import com.awake.util.JsonUtils;
import com.awake.util.base.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * @version : 1.0
 * @ClassName: ConsistentHashTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/1 10:12
 **/
public class ConsistentHashTest {

    //待添加入Hash环的服务器列表
    private static final List<Pair<String, String>> servers = List.of(new Pair<>("192.168.0.0:111", "192.168.0.0:111")
            , new Pair<>("192.168.0.1:111", "192.168.0.1:111"), new Pair<>("192.168.0.2:111", "192.168.0.2:111"));

    private static final List<Pair<String, String>> nums = List.of(new Pair<>("1", "1"), new Pair<>("2", "2"), new Pair<>("3", "3"));

    private static final List<Pair<String, String>> chars = List.of(new Pair<>("a", "a"), new Pair<>("b", "b"), new Pair<>("c", "c"));

    @Test
    public void consistentHashTest() {
        test(servers);
        System.out.println(StringUtils.MULTIPLE_HYPHENS);
        test(nums);
        System.out.println(StringUtils.MULTIPLE_HYPHENS);
        test(chars);
        System.out.println(StringUtils.MULTIPLE_HYPHENS);
    }

    /**
     * 测试分散性 测试哈希一致性
     * @param list
     */
    public void test(List<Pair<String, String>> list) {
        var realNodeMap = new HashMap<String, Integer>();
        var hitNodeMap = new HashMap<Integer, String>();

        list.forEach(it -> realNodeMap.put(it.getKey(), 0));

        var consistentHash = new ConsistentHash<>(list, 300);
        for (int i = 0; i < 100000; i++) {
            var key = String.valueOf(i);
            var realNode = consistentHash.getRealNode(key).getKey();

            int nums = realNodeMap.get(realNode);
            realNodeMap.put(realNode, ++nums);
            hitNodeMap.put(i, realNode);
        }
        // 测试一致性
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 100000; j++) {
                var key = String.valueOf(j);
                var realNode = consistentHash.getRealNode(key).getKey();
                Assert.assertEquals(realNode, hitNodeMap.get(j));
            }
        }

        System.out.println(JsonUtils.object2String(realNodeMap));
    }


    @Test
    public void testTreeMap() {
        var treeMap = new TreeMap<Integer, String>();
        treeMap.put(1, "a");
        treeMap.put(100, "b");
        treeMap.put(200, "c");

        System.out.println(treeMap.ceilingEntry(8));
        System.out.println(treeMap.ceilingEntry(450));

    }

}
