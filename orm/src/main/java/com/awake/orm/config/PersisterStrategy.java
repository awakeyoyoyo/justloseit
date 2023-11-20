package com.awake.orm.config;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: Persister
 * @Description: 持久化策略
 * @Auther: awake
 * @Date: 2023/11/20 17:47
 **/
@Data
public class PersisterStrategy {
    private String strategy;

    private PersisterTypeEnum type;

    private String config;
}
