package com.awake.orm.query;

import com.awake.orm.model.IEntity;

/**
 * @version : 1.0
 * @ClassName: MongodbQuery
 * @Description: 查询封装-建造者模式
 * @Auther: awake
 * @Date: 2023/11/22 18:27
 **/
public class MongodbQuery implements IQuery {

    @Override
    public <E extends IEntity<?>> IQueryBuilder<E> builder(Class<E> entityClazz) {
        return new MongoQueryBuilder<E>(entityClazz);
    }

}

