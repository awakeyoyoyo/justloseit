package com.awake.server.tcp.client;

import com.awake.module.GameModule;
import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.awake.packet.TestMsg;
import com.awake.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author：lqh
 * @Date：2024/3/27 17:31
 */
@Component
public class TcpClientController {


    private static final Logger logger = LoggerFactory.getLogger(TcpClientController.class);

    @PacketReceiver(protoId = GameModule.TcpHelloResponse)
    public void atTcpHelloResponse(Session session, TestMsg.TcpHelloResponse response) throws Exception {
        logger.info("tcp client receive [packet msg:{}] from server", response.getMsg());
    }

}
