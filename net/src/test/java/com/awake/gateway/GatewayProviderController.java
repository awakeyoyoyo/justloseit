package com.awake.gateway;

import com.awake.NetContext;
import com.awake.gateway.packet.GatewayToProviderRequest;
import com.awake.gateway.packet.GatewayToProviderResponse;
import com.awake.net.router.attachment.GatewayAttachment;
import com.awake.net.router.receiver.PacketReceiver;
import com.awake.net.session.Session;
import com.awake.util.JsonUtils;
import com.awake.util.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: GatewayProviderController
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/3 15:16
 **/

@Component
public class GatewayProviderController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayProviderController.class);

    /**
     * 注意：这里第2个请求参数以Request结尾，那么第3个参数必须是 GatewayAttachment类型(参加：PacketBus中扫描时的校验)
     */
    @PacketReceiver
    public void atGatewayToProviderRequest(Session session, GatewayToProviderRequest request, GatewayAttachment gatewayAttachment) {
        logger.info("provider receive [packet:{}] from client", JsonUtils.object2String(request));

        var response = new GatewayToProviderResponse();
        response.setMessage(StringUtils.format("Hello, this is the [provider:{}] response!", NetContext.getConfigManager().getNetConfig().toLocalRegisterVO().toString()));

        NetContext.getRouter().send(session, response, gatewayAttachment);
    }
}
