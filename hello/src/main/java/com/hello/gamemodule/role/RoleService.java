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
import com.hello.common.ErrorFactory;
import com.hello.gamemodule.role.entity.RoleEntity;
import com.hello.common.GameProtoId;
import com.hello.packet.LoginMsg;
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
            NetContext.getRouter().send(session, GameProtoId.ErrorResponse,
                    ErrorFactory.create(ErrorCode.ERROR_PARAMS));
            return;
        }

        NetContext.getRouter().send(session, GameProtoId.LoginResponse,
                LoginMsg.LoginResponse.newBuilder()
                        .setRid(roleEntity.getRid())
                        .setPassword(roleEntity.getPassword())
                        .setUserName(roleEntity.getId()).build());
    }

    public void atRegisterRequest(Session session, String userName, String password) {
        for (FilterWordResource filterWordResource : filterWordResources.getAll()) {
            if (userName.contains(filterWordResource.getFilter())) {
                NetContext.getRouter().send(session, GameProtoId.ErrorResponse,
                        ErrorFactory.create(ErrorCode.USER_NAME_ILLEGAL));
                return;
            }
        }
        RoleEntity roleEntity = roleEntityEntityCache.loadOrCreate(userName);
//        if (!StringUtils.isEmpty(roleEntity.id())) {
//            NetContext.getRouter().send(session, GameProtoId.ErrorResponse,
//                    ErrorFactory.create(ErrorCode.USER_NAME_EXIT));
//            return;
//        }

        roleEntity.setRid(GameContext.getIns().getIdManager().generalRoleId());
        roleEntity.setPassword(password);
        roleEntity.setId(userName);

        OrmContext.getAccessor().update(roleEntity);

        LoginMsg.RegisterResponse.Builder response = LoginMsg.RegisterResponse.newBuilder().setRid(roleEntity.getRid()).setPassword(roleEntity.getPassword())
                .setUserName(roleEntity.getId());
        NetContext.getRouter().send(session, GameProtoId.RegisterResponse,
                response.build());
    }
}
