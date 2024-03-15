package com.awake.net2.server;


import com.awake.util.IOUtils;
import com.awake.util.base.ThreadUtils;
import com.awake.util.net.HostAndPort;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author：lqh
 * @Date：2024/3/15 11:01
 */
public abstract class AbstractServer <C extends Channel> extends ChannelInitializer<C> implements IServer {
    private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    protected String hostAddress;

    protected int port;

    /**
     * 所有的服务器都可以在这个列表中取到
     */
    protected static final List<AbstractServer<? extends Channel>> allServers = new CopyOnWriteArrayList<>();

    /**
     * 配置服务端nio线程组，服务端接受客户端连接
     */
    private EventLoopGroup bossGroup;

    /**
     * SocketChannel的网络读写
     */
    protected EventLoopGroup workerGroup;

    protected ChannelFuture channelFuture;

    public AbstractServer(HostAndPort host) {
        this.hostAddress = host.getHost();
        this.port = host.getPort();
    }

    @Override
    public void start() {
        doStart();
    }

    protected synchronized void doStart() {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        // 一条线程持有一个端口对应的selector，如果我们启动不仅仅是一个服务器端口的话，为了更好的性能需要修改对应的bossGroup数量
        bossGroup = Epoll.isAvailable()
                ? new EpollEventLoopGroup(Math.max(1, cpuNum / 8), new DefaultThreadFactory("netty-boss", true))
                : new NioEventLoopGroup(Math.max(1, cpuNum / 8), new DefaultThreadFactory("netty-boss", true));

        workerGroup = Epoll.isAvailable()
                ? new EpollEventLoopGroup(cpuNum * 2, new DefaultThreadFactory("netty-worker", true))
                : new NioEventLoopGroup(cpuNum * 2, new DefaultThreadFactory("netty-worker", true));

        ServerBootstrap bootstrap = new ServerBootstrap();
        /**
         * 1）channel 能用Linux epoll机制  不能就Java NIO
         * 2）option(ChannelOption.SO_REUSEADDR,true) 地址可以复用
         * 3）childOption(ChannelOption.TCP_NODELAY, true) 禁用Nagle算法   实时发送不管包大小
         * 4）childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_MB))
         *    当Channel write向缓冲区写入数据的时候，如果数据量超过了设置的高水位值，就设置通道Channel不可写状态。
         *    当Channel flush将缓冲区中的数据写入到TCP缓冲区之后，如果Netty缓冲区的数据量低于低水位值时，就设置通过Channel可写状态。
         *    Netty默认设置的高水位为64KB，低水位为32KB。
         *    此处设置低水位16KB 高水位16MB    即TCP缓存区大小小于16kb可写    缓冲区大小小于16MB的时候可写
         * 5) childHandler()是发生在客户端连接之后 调用initChannel
         * */
        bootstrap.group(bossGroup, workerGroup)
                .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_MB))
                .childHandler(this);
        // 绑定端口，同步等待成功
        // channelFuture = bootstrap.bind(hostAddress, port).sync();
        // 等待服务端监听端口关闭
        // channelFuture.channel().closeFuture().sync();


        // 异步
        channelFuture = bootstrap.bind(hostAddress, port);
        channelFuture.syncUninterruptibly();

        allServers.add(this);

        logger.info("{} started at [{}:{}]", this.getClass().getSimpleName(), hostAddress, port);
    }

    @Override
    public void shutdown() {
        ThreadUtils.shutdownEventLoopGracefully("netty-boss", bossGroup);
        ThreadUtils.shutdownEventLoopGracefully("netty-worker", workerGroup);

        if (channelFuture != null) {
            try {
                channelFuture.channel().close().syncUninterruptibly();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    public static void shutdownAllServers() {
        allServers.forEach(it -> it.shutdown());
    }
}
