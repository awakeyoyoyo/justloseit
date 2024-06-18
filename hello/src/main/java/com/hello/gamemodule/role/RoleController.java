package com.hello.gamemodule.role;

import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.hello.common.GameProtoId;
import com.hello.packet.LoginMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author：lqh
 * @Date：2024/4/1 16:08
 */
@Controller
public class RoleController {


    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PacketReceiver(protoId = GameProtoId.LoginRequest)
    public void atLoginRequest(Session session, LoginMsg.LoginRequest loginRequest) throws Exception {
        roleService.atLoginRequest(session,loginRequest.getUserName(),loginRequest.getPassword());
    }

    @PacketReceiver(protoId = GameProtoId.RegisterRequest)
    public void atRegisterRequest(Session session, LoginMsg.RegisterRequest registerRequest) throws Exception {
        roleService.atRegisterRequest(session,registerRequest.getUserName(),registerRequest.getPassword());
    }
}
