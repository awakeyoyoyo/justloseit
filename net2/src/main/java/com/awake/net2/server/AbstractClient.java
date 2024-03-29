package com.awake.net2.server;

import com.awake.net2.NetContext;
import com.awake.net2.session.Session;
import com.awake.util.ExceptionUtils;
import com.awake.util.IOUtils;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author：lqh
 * @Date：2024/3/15 11:01
 */
public abstract class AbstractClient <C extends Channel> extends ChannelInitializer<C> implements IClient {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

    protected static final EventLoopGroup nioEventLoopGroup = Epoll.isAvailable()
            ? new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() + 1, new DefaultThreadFactory("netty-client", true))
            : new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() + 1, new DefaultThreadFactory("netty-client", true));

    protected String hostAddress;
    protected int port;

    protected Bootstrap bootstrap;

    public AbstractClient(HostAndPort host) {
        this.hostAddress = host.getHost();
        this.port = host.getPort();
    }

    @Override
    public Session start() {
        return doStart();
    }

    private Session doStart() {
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(nioEventLoopGroup)
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_MB))
                .handler(this);
        ChannelFuture channelFuture = bootstrap.connect(hostAddress, port);
        channelFuture.syncUninterruptibly();

        if (channelFuture.isSuccess()) {
            if (channelFuture.channel().isActive()) {
                Channel channel = channelFuture.channel();
                var session = Session.valueOf(channel);
                NetContext.getSessionManager().addClientSession(session);
                logger.info("{} started at [{}]", this.getClass().getSimpleName(), channel.localAddress());
                return session;
            }
        } else if (channelFuture.cause() != null) {
            logger.error(ExceptionUtils.getMessage(channelFuture.cause()));
        } else {
            logger.error("[{}] started failed", this.getClass().getSimpleName());
        }
        return null;
    }


    public static void shutdown() {
        ThreadUtils.shutdownEventLoopGracefully("netty-client", nioEventLoopGroup);
    }
}
