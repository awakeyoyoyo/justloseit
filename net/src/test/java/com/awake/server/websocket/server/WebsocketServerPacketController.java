/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.awake.server.websocket.server;


import com.awake.NetContext;
import com.awake.constant.ModuleId;
import com.awake.net.router.receiver.PacketController;
import com.awake.net.router.receiver.PacketReceiver;
import com.awake.net.session.Session;
import com.awake.server.websocket.packet.WebsocketHelloRequest;
import com.awake.server.websocket.packet.WebsocketHelloResponse;
import com.awake.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author awake
 */
@Component
@PacketController(moduleId = ModuleId.gameModule)
public class WebsocketServerPacketController {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketServerPacketController.class);

    @PacketReceiver(protocolId = WebsocketHelloRequest.PROTOCOL_ID)
    public void atWebsocketHelloRequest(Session session, WebsocketHelloRequest request) {
        logger.info("receive [packet:{}] from browser", JsonUtils.object2String(request));

        var response = new WebsocketHelloResponse();
        response.setMessage("Hello, this is the websocket server! -> " + request.getMessage());

        NetContext.getRouter().send(session, response);
    }

}
