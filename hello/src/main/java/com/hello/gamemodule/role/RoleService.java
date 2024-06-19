package com.hello.gamemodule.role;

import com.awake.event.manger.EventBus;
import com.awake.net2.NetContext;
import com.awake.net2.session.Session;
import com.awake.orm.OrmContext;
import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.cache.EntityCache;
import com.awake.storage.anno.StorageAutowired;
import com.awake.storage.model.IStorage;
import com.hello.GameContext;
import com.hello.common.ErrorCode;
import com.hello.common.ErrorResponseFactory;
import com.hello.gamemodule.dailyreset.event.RoleDailyResetEvent;
import com.hello.gamemodule.role.event.LoginEvent;
import com.hello.gamemodule.role.event.LogoutEvent;
import com.hello.gamemodule.role.entity.RoleEntity;
import com.hello.common.GameProtoId;
import com.hello.packet.LoginMsg;
import com.hello.resource.FilterWordResource;
import com.hello.util.DateUtil;
import org.springframework.stereotype.Service;

/**
 * @Author：lqh
 * @Date：2024/4/1 16:09
 */
@Service
public class RoleService {
    @EntityCacheAutowired
    private EntityCache<Long, RoleEntity> roleEntityEntityCache;

    @StorageAutowired
    public IStorage<Integer, FilterWordResource> filterWordResources;

    private final RoleManager roleManager;

    public RoleService(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void atLoginRequest(Session session, String userName, String password) {
        RoleEntity roleEntity = OrmContext.getQuery(RoleEntity.class).eq("password", password).eq("userName", userName).queryFirst();
        if (roleEntity == null) {
            NetContext.getRouter().send(session, GameProtoId.ErrorResponse,
                    ErrorResponseFactory.create(ErrorCode.ERROR_PARAMS));
            return;
        }
        roleEntity = roleEntityEntityCache.load(roleEntity.id());
        session.setUserId(roleEntity.id());
        Session oldSession = roleManager.getSession(roleEntity.id());
        if (oldSession != null) {
            //手动登出处理
            doLogout(oldSession);
            //关闭连接
            oldSession.close();
        }
        //执行登录逻辑
        doLogin(session);
        NetContext.getRouter().send(session, GameProtoId.LoginResponse,
                LoginMsg.LoginResponse.newBuilder()
                        .setRid(roleEntity.id())
                        .setPassword(roleEntity.getPassword())
                        .setUserName(roleEntity.getUserName()).build());
    }

    public void atRegisterRequest(Session session, String userName, String password) {
        for (FilterWordResource filterWordResource : filterWordResources.getAll()) {
            if (userName.contains(filterWordResource.getFilter())) {
                NetContext.getRouter().send(session, GameProtoId.ErrorResponse,
                        ErrorResponseFactory.create(ErrorCode.USER_NAME_ILLEGAL));
                return;
            }
        }

        RoleEntity roleEntity = OrmContext.getQuery(RoleEntity.class).eq("userName", userName).queryFirst();
        if (roleEntity != null) {
            NetContext.getRouter().send(session, GameProtoId.ErrorResponse,
                    ErrorResponseFactory.create(ErrorCode.USER_NAME_EXIT));
            return;
        }

        roleEntity=roleEntityEntityCache.loadOrCreate(GameContext.getIns().getIdManager().generalRoleId());
        roleEntity.setId(GameContext.getIns().getIdManager().generalRoleId());
        roleEntity.setUserName(userName);
        roleEntity.setPassword(password);

        OrmContext.getAccessor().update(roleEntity);

        LoginMsg.RegisterResponse.Builder response = LoginMsg.RegisterResponse.newBuilder().setRid(roleEntity.id()).setPassword(roleEntity.getPassword())
                .setUserName(roleEntity.getUserName());
        NetContext.getRouter().send(session, GameProtoId.RegisterResponse,
                response.build());
    }

    /**
     * 登出
     *
     * @param session
     */
    public void doLogout(Session session) {
        if (session.getUserId() == 0) {
            return;
        }
        //抛出登出事件
        EventBus.publicEvent(LogoutEvent.valueOf(session));
    }

    /**
     * 登录
     *
     * @param session
     */
    private void doLogin(Session session) {
        roleManager.addSession(session.getUserId(), session);
        //每日重置下
        doRoleDailyReset(session);

        //登录事件
        EventBus.publicEvent(LoginEvent.valueOf(session));
    }

    /**
     * 玩家每日重置
     *
     * @param session
     */
    public void doRoleDailyReset(Session session) {
        RoleEntity roleEntity = roleEntityEntityCache.load(session.getUserId());
        long currTime = DateUtil.getCurrentTime();
        if (DateUtil.isSameDay(currTime, roleEntity.getDailyResetTime())) {
            return;
        }
        roleEntity.setDailyResetTime(currTime);
        roleEntityEntityCache.update(roleEntity);

        EventBus.publicEvent(RoleDailyResetEvent.valueOf(session));
    }
}
