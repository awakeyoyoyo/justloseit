package com.hello.gamemodule.role;

import com.awake.net2.session.Session;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
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
}
