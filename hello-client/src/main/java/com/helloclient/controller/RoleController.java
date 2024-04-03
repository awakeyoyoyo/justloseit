package com.helloclient.controller;

import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.helloclient.packet.ErrorResponse;
import com.helloclient.packet.LoginResponse;
import com.helloclient.packet.RegisterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author：lqh
 * @Date：2024/4/3 11:32
 */
@Controller
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @PacketReceiver
    public void atLoginResponse(Session session, LoginResponse loginResponse) throws Exception {
        logger.info("login response:{}",loginResponse.toString());
    }

    @PacketReceiver
    public void atRegisterResponse(Session session, RegisterResponse registerResponse) throws Exception {
        logger.info("register response:{}",registerResponse.toString());
    }


    @PacketReceiver
    public void atErrorResponse(Session session, ErrorResponse errorResponse) throws Exception {
        logger.info("register response:{}",errorResponse.toString());
    }
}

