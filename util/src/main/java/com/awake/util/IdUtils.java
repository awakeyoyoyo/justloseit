package com.awake.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version : 1.0
 * @ClassName: IdUtils
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/3/10 16:26
 **/
public class IdUtils {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    /**
     * 获取本地int的唯一id，如果达到最大值则重新从最小值重新计算，线程安全
     */
    public static int getLocalIntId() {
        return ATOMIC_INTEGER.incrementAndGet();
    }


    /**
     * 获得分布式环境下唯一id
     *
     * @return String UUID
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }


    /**
     * 小的id在前 - 大的id在后
     *
     * @param a 第一个数字
     * @param b 第二个数字
     * @return 生成的id
     */
    public static String generateStringId(long a, long b) {
        return Math.min(a, b) + "-" + Math.max(a, b);
    }

}
