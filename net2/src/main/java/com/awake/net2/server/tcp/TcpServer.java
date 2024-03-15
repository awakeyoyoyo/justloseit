package com.awake.net2.server.tcp;

import com.awake.net2.handler.ServerRouterHandler;
import com.awake.net2.handler.codec.JProtobufTcpCodecHandler;
import com.awake.net2.handler.idle.ServerIdleHandler;
import com.awake.net2.server.AbstractServer;
import com.awake.util.net.HostAndPort;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Author：lqh
 * @Date：2024/3/15 10:49
 */
public class TcpServer extends AbstractServer<SocketChannel> {

    public TcpServer(HostAndPort host) {
        super(host);
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast(new IdleStateHandler(0, 0, 180));
        //心跳处理器
        channel.pipeline().addLast(new ServerIdleHandler());
        //协议解析
        channel.pipeline().addLast(new JProtobufTcpCodecHandler());
        //逻辑处理
        channel.pipeline().addLast(new ServerRouterHandler());
    }

    @Override
    public void start() {

    }
}

