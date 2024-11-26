package com.hello.gamemodule.Id;

import com.awake.orm.OrmContext;
import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.cache.EntityCache;
import com.hello.GameContext;
import com.hello.define.ManagerOrder;
import com.hello.frame.manger.IModuleManager;
import com.hello.gamemodule.Id.entity.IdEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author：lqh
 * @Date：2024/4/2 10:55
 */
@Component
public class IdManager implements IModuleManager {


    @EntityCacheAutowired
    private EntityCache<Integer, IdEntity> idEntityEntityCache;

    /**
     * 角色原子id
     */
    private AtomicLong atomicRoleId;
    /**
     * 玩家id
     * @return
     */
    public long generalRoleId(){
        int serverId = GameContext.getIns().getGameServerProperties().getServerId();
        long id = atomicRoleId.incrementAndGet();
        IdEntity roleIdEntity = idEntityEntityCache.load(IdConstant.ROLE_ID);
        roleIdEntity.setValue(atomicRoleId.get());
        idEntityEntityCache.update(roleIdEntity);
        return Long.parseLong(String.valueOf(serverId) + id);
    }

    @Override
    public int order() {
        return ManagerOrder.HIGH_LEVEL;
    }

    @Override
    public void init() {
        IdEntity roleIdEntity = idEntityEntityCache.load(IdConstant.ROLE_ID);
        //初始化
        if (roleIdEntity.empty()) {
            roleIdEntity.setId(IdConstant.ROLE_ID);
            roleIdEntity.setValue(0);
            OrmContext.getAccessor().insert(roleIdEntity);
        }
        atomicRoleId = new AtomicLong(roleIdEntity.getValue());
    }

    @Override
    public void shutDown() {
        IdEntity roleIdEntity = idEntityEntityCache.load(IdConstant.ROLE_ID);
        if (roleIdEntity.getValue()==atomicRoleId.get()){
            return;
        }
        roleIdEntity.setValue(atomicRoleId.get());
        idEntityEntityCache.update(roleIdEntity);
    }
}
