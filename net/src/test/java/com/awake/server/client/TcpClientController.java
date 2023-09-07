package com.awake.server.client;

import com.awake.net.router.receiver.PacketReceiver;
import com.awake.net.session.Session;
import com.awake.server.packet.tcp.TcpHelloResponse;
import com.awake.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: TcoClientController
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 16:26
 **/
@Component
public class TcpClientController {


    private static final Logger logger = LoggerFactory.getLogger(TcpClientController.class);

    @PacketReceiver
    public void atTcpHelloResponse(Session session, TcpHelloResponse response) {
        logger.info("tcp client receive [packet:{}] from server", JsonUtils.object2String(response));
    }

}