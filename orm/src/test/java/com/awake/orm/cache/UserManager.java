package com.awake.orm.cache;

import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: UserManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 17:41
 **/
@Component
public class UserManager {

    @EntityCacheAutowired
    public IEntityCache<Long, UserEntity> userEntityCaches;
}
