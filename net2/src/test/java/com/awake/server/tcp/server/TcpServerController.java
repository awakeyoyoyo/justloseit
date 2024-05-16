package com.awake.server.tcp.server;

import com.awake.module.GameModule;
import com.awake.net2.NetContext;
import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.awake.packet.TestMsg;
import com.awake.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author：lqh
 * @Date：2024/3/27 19:35
 */
@Component
public class TcpServerController {

    private static final Logger logger = LoggerFactory.getLogger(TcpServerController.class);

    @PacketReceiver(protoId = GameModule.TcpHelloRequest)
    public void atTcpHelloRequest(Session session, TestMsg.TcpHelloRequest request) {
        logger.info("receive [packet msg:{}] from client", request.getMsg());

        var response = TestMsg.TcpHelloResponse.newBuilder().setMsg("Hello, this is the tcp server!");


        NetContext.getRouter().send(session, GameModule.TcpHelloResponse, response.build());
    }
}
