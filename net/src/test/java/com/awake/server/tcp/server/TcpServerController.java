package com.awake.server.tcp.server;

import com.awake.NetContext;
import com.awake.net.router.receiver.PacketReceiver;
import com.awake.net.session.Session;
import com.awake.server.tcp.packet.tcp.TcpHelloRequest;
import com.awake.server.tcp.packet.tcp.TcpHelloRequest1;
import com.awake.server.tcp.packet.tcp.TcpHelloResponse;
import com.awake.server.tcp.packet.tcp.TcpHelloResponse1;
import com.awake.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: TcpServerController
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:27
 **/
@Component
public class TcpServerController {

    private static final Logger logger = LoggerFactory.getLogger(TcpServerController.class);

    @PacketReceiver
    public void atTcpHelloRequest(Session session, TcpHelloRequest request) {
        logger.info("receive [packet:{}] from client", JsonUtils.object2String(request));

        var response = new TcpHelloResponse();
        response.setMessage("Hello, this is the tcp server!");

        NetContext.getRouter().send(session, response);
    }

    @PacketReceiver
    public void atTcpHelloRequest1(Session session, TcpHelloRequest1 request) {
        logger.info("receive [packet:{}] from client", JsonUtils.object2String(request));

        var response = new TcpHelloResponse1();
        response.setMessage("Hello, this is the tcp server! awake awake awake");

        NetContext.getRouter().send(session, response);
    }
}

