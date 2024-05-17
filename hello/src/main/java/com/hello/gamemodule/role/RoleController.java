package com.hello.gamemodule.role;

import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.hello.common.module.GameModule;
import com.hello.packet.LoginMsg;
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

    @PacketReceiver(protoId = GameModule.LoginRequest)
    public void atLoginRequest(Session session, LoginMsg.LoginRequest loginRequest) throws Exception {
        roleService.atLoginRequest(session,loginRequest.getUserName(),loginRequest.getPassword());
    }

    @PacketReceiver(protoId = GameModule.RegisterRequest)
    public void atRegisterRequest(Session session, LoginMsg.RegisterRequest registerRequest) throws Exception {
        roleService.atRegisterRequest(session,registerRequest.getUserName(),registerRequest.getPassword());
    }
}
