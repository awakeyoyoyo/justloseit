package com.awake.orm.cache;

import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.entity.UserEntity;
import com.awake.orm.entity.bag.MapEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: UserManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 17:41
 **/
@Component
@Data
public class UserManager {

    @EntityCacheAutowired
    public IEntityCache<Long, UserEntity> userEntityCaches;

    @EntityCacheAutowired
    public IEntityCache<Long, MapEntity> mapEntityIEntityCache;
}
