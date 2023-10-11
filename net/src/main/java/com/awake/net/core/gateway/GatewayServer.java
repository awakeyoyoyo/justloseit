package com.awake.net.core.gateway;

import com.awake.net.codec.Jprotobuf.JProtobufTcpCodecHandler;
import com.awake.net.core.gateway.handler.GatewayRouteHandler;
import com.awake.net.router.handler.idle.ServerIdleHandler;
import com.awake.net.server.AbstractServer;
import com.awake.net.session.Session;
import com.awake.util.net.HostAndPort;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.lang.Nullable;

import java.util.function.BiFunction;

/**
 * @version : 1.0
 * @ClassName: GateWayServer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 18:02
 **/
public class GatewayServer extends AbstractServer<SocketChannel> {

    private final BiFunction<Session, Object, Boolean> packetFilter;

    public GatewayServer(HostAndPort host, @Nullable BiFunction<Session, Object, Boolean> packetFilter) {
        super(host);
        this.packetFilter = packetFilter;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new IdleStateHandler(0, 0, 180));
        channel.pipeline().addLast(new ServerIdleHandler());
        channel.pipeline().addLast(new JProtobufTcpCodecHandler());
        channel.pipeline().addLast(new GatewayRouteHandler(packetFilter));
    }

}

