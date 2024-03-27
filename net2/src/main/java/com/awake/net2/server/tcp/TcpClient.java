package com.awake.net2.server.tcp;

import com.awake.net2.handler.ClientRouterHandler;
import com.awake.net2.handler.codec.JProtobufTcpCodecHandler;
import com.awake.net2.handler.idle.ClientIdleHandler;
import com.awake.net2.server.AbstractClient;
import com.awake.util.net.HostAndPort;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Author：lqh
 * @Date：2024/3/15 10:50
 */
public class TcpClient extends AbstractClient<SocketChannel> {

    public TcpClient(HostAndPort host) {
        super(host);
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        // 可以看出来，这个客户端检测到空闲的时间是60s，相对短一点，这样子就可以发送心跳。
        // 服务器端则是180s，相对长一点，一旦检测到空闲，则把客户端踢掉。
        channel.pipeline().addLast(new IdleStateHandler(0, 0, 60));
        channel.pipeline().addLast(new ClientIdleHandler());
        channel.pipeline().addLast(new JProtobufTcpCodecHandler());
        channel.pipeline().addLast(new ClientRouterHandler());
    }
}


