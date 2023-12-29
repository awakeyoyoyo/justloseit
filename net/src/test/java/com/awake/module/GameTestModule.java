package com.awake.module;

/**
 * @version : 1.0
 * @ClassName: GameTestModule
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/21 10:43
 **/
public interface GameTestModule {
    int ModuleId = 1;

    int TcpHelloRequest = 2000;
    int TcpHelloResponse = 2001;
    int TcpHelloRequest1 = 2002;
    int TcpHelloResponse1 = 2003;

    int WebsocketHelloRequest=1600;
    int WebsocketHelloResponse=1601;

    int GatewayToProviderRequest=5001;
    int GatewayToProviderResponse=5002;

    int ProviderMessAsk=1300;
    int ProviderMessAnswer=1301;
}
