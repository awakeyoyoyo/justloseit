package com.awake.orm.config;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: CacheStrategy
 * @Description: 缓存策略
 * @Auther: awake
 * @Date: 2023/11/20 17:47
 **/
@Data
public class CacheStrategy {

    private String strategy;
    private int size;
    private long expireMillisecond;
}
