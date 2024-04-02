package com.hello.gamemodule.role;

import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.hello.packet.LoginRequest;
import com.hello.packet.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author：lqh
 * @Date：2024/4/1 16:08
 */
@Controller
public class RoleController {

    @Autowired
    private  RoleService roleService;

    @PacketReceiver
    public void atLoginRequest(Session session, LoginRequest loginRequest) throws Exception {
        roleService.atLoginRequest(session,loginRequest.getUserName(),loginRequest.getPassword());
    }

    @PacketReceiver
    public void atRegisterRequest(Session session, RegisterRequest registerRequest) throws Exception {
        roleService.atRegisterRequest(session,registerRequest.getUserName(),registerRequest.getPassword());
    }
}
