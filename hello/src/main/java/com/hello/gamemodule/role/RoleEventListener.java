package com.hello.gamemodule.role;

import com.awake.event.anno.EventReceiver;
import com.awake.net2.event.ServerSessionInactiveEvent;
import com.awake.net2.router.TaskBus;
import com.awake.net2.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author：lqh
 * @Date：2024/6/17 20:21
 */
@Component
public class RoleEventListener {

    @Autowired
    private RoleService roleService;

    @EventReceiver
    public void onServerSessionInactiveEvent(ServerSessionInactiveEvent event) {
        Session session = event.getSession();
        long userId = session.getUserId();
        TaskBus.execute((int) userId, () -> roleService.doLogout(session));
    }
}
