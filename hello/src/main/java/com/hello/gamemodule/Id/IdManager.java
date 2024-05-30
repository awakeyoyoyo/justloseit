package com.hello.gamemodule.Id;

import com.awake.orm.OrmContext;
import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.cache.EntityCache;
import com.hello.GameContext;
import com.hello.gamemodule.Id.entity.IdEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:55
 */
@Component
public class IdManager {


    @EntityCacheAutowired
    private EntityCache<Integer, IdEntity> idEntityEntityCache;

    /**
     * 角色原子id
     */
    private AtomicLong atomicRoleId;

    /**
     * 初始化id
     */
    public void initIdValue() {
        IdEntity roleIdEntity = idEntityEntityCache.load(IdConstant.ROLE_ID);
        //初始化
        if (roleIdEntity.empty()) {
            roleIdEntity.setId(IdConstant.ROLE_ID);
            roleIdEntity.setValue(0);
            OrmContext.getAccessor().insert(roleIdEntity);
        }
        atomicRoleId = new AtomicLong(roleIdEntity.getValue());
    }

    /**
     * 保存id值
     */
    public void saveIdValue() {
        IdEntity roleIdEntity = idEntityEntityCache.load(IdConstant.ROLE_ID);
        if (roleIdEntity.getValue()==atomicRoleId.get()){
            return;
        }
        roleIdEntity.setValue(atomicRoleId.get());
        idEntityEntityCache.update(roleIdEntity);
    }

    /**
     * 玩家id
     * @return
     */
    public long generalRoleId(){
        int serverId = GameContext.getIns().getGameServerProperties().getServerId();
        atomicRoleId.incrementAndGet();
        IdEntity roleIdEntity = idEntityEntityCache.load(IdConstant.ROLE_ID);
        roleIdEntity.setValue(atomicRoleId.get());
        idEntityEntityCache.update(roleIdEntity);
        return Long.parseLong(String.valueOf(serverId) + atomicRoleId.incrementAndGet());
    }
}
