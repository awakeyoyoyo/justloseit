package com.awake.orm.cache.persister;

import com.awake.orm.config.PersisterTypeEnum;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: PersisterStrategy
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/15 16:11
 **/
@Data
public class PersisterStrategy {
    private String strategy;

    private PersisterTypeEnum type;

    private String config;

    public PersisterStrategy() {
    }

    public PersisterStrategy(String strategy, String type, String config) {
        this.strategy = strategy;
        this.config = config;
        this.type = PersisterTypeEnum.getPersisterType(type);
    }

}
