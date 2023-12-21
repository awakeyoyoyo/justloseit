package com.awake.net.module;

/**
 * @version : 1.0
 * @ClassName: BaseRouteHandler
 * @Description: 通用模塊id 以及 通用模塊包協議id
 * @Auther: awake
 * @Date: 2023/8/3 18:24
 **/
public interface CommonModule {

    int ModuleId = 0;

    int SignalAttachment = 1;

    int SignalOnlyAttachment = 2;

    int GatewayAttachment = 3;

    int NoAnswerAttachment=5;

    int AuthUidToGatewayCheck = 20;

    int AuthUidToGatewayConfirm = 21;

    int AuthUidAsk = 22;

    int GatewaySessionInactiveAsk = 23;

    int GatewaySynchronizeSidAsk = 24;

    int Error = 101;

    int Heartbeat = 102;

    int Ping = 103;

    int Pong = 104;
}
