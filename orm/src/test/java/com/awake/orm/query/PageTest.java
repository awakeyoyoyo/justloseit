package com.awake.orm.query;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @version : 1.0
 * @ClassName: PageTest
 * @Description: 页数测试
 * @Auther: awake
 * @Date: 2023/11/30 16:37
 **/
public class PageTest {

    /**
     * 页数测试
     */
    @Test
    public void pageTest() {
        var list = new ArrayList<Integer>();
        for (var i = 1; i <= 105; i++) {
            list.add(i);
        }

        var page = Page.valueOf(11, 10, list.size());
        var result = page.currentPageList(list).toArray();

        Assert.assertArrayEquals(List.of(101, 102, 103, 104, 105).toArray(), result);
    }
}
