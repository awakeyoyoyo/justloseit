package com.hello.gamemodule.role;

import com.awake.net2.NetContext;
import com.awake.net2.session.Session;
import com.awake.orm.OrmContext;
import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.cache.EntityCache;
import com.awake.storage.anno.StorageAutowired;
import com.awake.storage.model.IStorage;
import com.awake.util.base.StringUtils;
import com.hello.GameContext;
import com.hello.common.ErrorCode;
import com.hello.entity.RoleEntity;
import com.hello.packet.ErrorResponse;
import com.hello.packet.LoginResponse;
import com.hello.packet.RegisterResponse;
import com.hello.module.GameModule;
import com.hello.resource.FilterWordResource;
import org.springframework.stereotype.Service;

/**
 * @Author：lqh
 * @Date：2024/4/1 16:09
 */
@Service
public class RoleService {
    @EntityCacheAutowired
    private EntityCache<String, RoleEntity> roleEntityEntityCache;

    @StorageAutowired
    public IStorage<Integer, FilterWordResource> filterWordResources;

    public void atLoginRequest(Session session, String userName, String password) {
        RoleEntity roleEntity = roleEntityEntityCache.load(userName);
        if (StringUtils.isEmpty(roleEntity.id()) || !roleEntity.getPassword().equals(password)) {
            NetContext.getRouter().send(session, GameModule.ErrorResponse,
                    ErrorResponse.valueOf(ErrorCode.ERROR_PARAMS));
            return;
        }

        NetContext.getRouter().send(session, GameModule.LoginResponse,
                LoginResponse.valueOf(roleEntity.getRid(), roleEntity.getId(), roleEntity.getPassword()));
    }

    public void atRegisterRequest(Session session, String userName, String password) {
        for (FilterWordResource filterWordResource : filterWordResources.getAll()) {
            if (userName.contains(filterWordResource.getFilter())) {
                NetContext.getRouter().send(session, GameModule.ErrorResponse,
                        ErrorResponse.valueOf(ErrorCode.USER_NAME_ILLEGAL));
                return;
            }
        }
        RoleEntity roleEntity = roleEntityEntityCache.load(userName);
        if (!StringUtils.isEmpty(roleEntity.id())) {
            NetContext.getRouter().send(session, GameModule.ErrorResponse,
                    ErrorResponse.valueOf(ErrorCode.USER_NAME_EXIT));
            return;
        }

        roleEntity.setRid(GameContext.getInstance().getIdManager().generalRoleId());
        roleEntity.setPassword(password);
        roleEntity.setId(userName);

        OrmContext.getAccessor().insert(roleEntity);

        NetContext.getRouter().send(session, GameModule.RegisterResponse,
                RegisterResponse.valueOf(roleEntity.getRid(), roleEntity.getId(), roleEntity.getPassword()));
    }
}
