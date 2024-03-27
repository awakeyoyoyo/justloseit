package com.awake.server.tcp.server;

import com.awake.module.GameModule;
import com.awake.net2.NetContext;
import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.awake.packet.tcp.TcpHelloRequest;
import com.awake.packet.tcp.TcpHelloResponse;
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

    @PacketReceiver
    public void atTcpHelloRequest(Session session, TcpHelloRequest request) {
        logger.info("receive [packet:{}] from client", JsonUtils.object2String(request));

        var response = new TcpHelloResponse();
        response.setMessage("Hello, this is the tcp server!");

        NetContext.getRouter().send(session, GameModule.TcpHelloResponse, response);
    }
}
