package com.awake.module;

/**
 * @Author：lqh
 * @Date：2024/3/27 17:19
 */
public interface GameModule {

    int ModuleId = 1;

    int TcpHelloRequest = 2000;
    int TcpHelloResponse = 2001;
    int TcpHelloRequest1 = 2002;
    int TcpHelloResponse1 = 2003;

    int WebsocketHelloRequest=1600;
    int WebsocketHelloResponse=1601;
}
