package com.awake.net.server.tcp;

import com.awake.net.handler.codec.Jprotobuf.JProtobufTcpCodecHandler;
import com.awake.net.handler.ServerRouteHandler;
import com.awake.net.handler.idle.ServerIdleHandler;
import com.awake.net.server.AbstractServer;
import com.awake.util.net.HostAndPort;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @version : 1.0
 * @ClassName: TcpServer
 * @Description: Tcp服务端
 * @Auther: awake
 * @Date: 2023/8/2 19:34
 **/
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
        channel.pipeline().addLast(new ServerRouteHandler());
    }
}
