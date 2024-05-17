package com.helloclient.controller;

import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.helloclient.packet.LoginMsg;
import com.helloclient.packet.RpcMsg;
import com.helloclient.protomodule.GameModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @Author：lqh
 * @Date：2024/4/3 11:32
 */
@Controller
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @PacketReceiver(protoId = GameModule.LoginResponse)
    public void atLoginResponse(Session session, LoginMsg.LoginResponse loginResponse) throws Exception {
        logger.info("login response:{}",loginResponse.toString());
    }

    @PacketReceiver(protoId = GameModule.RegisterResponse)
    public void atRegisterResponse(Session session, LoginMsg.RegisterResponse registerResponse) throws Exception {
        logger.info("register response:{}",registerResponse.toString());
    }


    @PacketReceiver(protoId = GameModule.ErrorResponse)
    public void atErrorResponse(Session session, RpcMsg.ErrorResponse errorResponse) throws Exception {
        logger.info("register response:{}",errorResponse.toString());
    }
}

