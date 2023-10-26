package com.awake.register.controller;

import com.awake.NetContext;
import com.awake.net.router.receiver.PacketReceiver;
import com.awake.net.session.Session;
import com.awake.register.packet.ProviderMessAnswer;
import com.awake.register.packet.ProviderMessAsk;
import com.awake.util.JsonUtils;
import com.awake.util.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: ProviderController
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/26 16:21
 **/
@Component
public class ProviderController {
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    @PacketReceiver
    public void atProviderMessAsk(Session session, ProviderMessAsk ask) {
        logger.info("provider receive [packet:{}] from consumer", JsonUtils.object2String(ask));

        var response = new ProviderMessAnswer();
        response.setMessage(StringUtils.format("Hello, this is the [provider:{}] answer!",
                NetContext.getConfigManager().getNetConfig().toLocalRegisterVO().toString()));

        NetContext.getRouter().send(session, response);
    }
}
