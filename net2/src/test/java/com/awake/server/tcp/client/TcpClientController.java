package com.awake.server.tcp.client;

import com.awake.module.GameModule;
import com.awake.net2.NetContext;
import com.awake.net2.router.receiver.PacketReceiver;
import com.awake.net2.session.Session;
import com.awake.packet.tcp.TcpHelloRequest1;
import com.awake.packet.tcp.TcpHelloResponse;
import com.awake.packet.tcp.TcpHelloResponse1;
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

    @PacketReceiver
    public void atTcpHelloResponse(Session session, TcpHelloResponse response) throws Exception {
        logger.info("tcp client receive [packet:{}] from server", JsonUtils.object2String(response));
    }

}
