package com.hello.gamemodule.role;

import com.awake.net2.session.Session;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家模块公共数据管理
 * @Author：lqh
 * @Date：2024/6/17 10:57
 */
@Component
public class RoleManager {


    private Map<Long, Session> roleId2Session=new ConcurrentHashMap<>();

    public Session getSession(long roleId){
        return roleId2Session.get(roleId);
    }

    public void addSession(long roleId, Session session){
        roleId2Session.put(roleId,session);
    }

    public List<Session> getOnlineRole(){
        return new ArrayList<>(roleId2Session.values());
    }
}
