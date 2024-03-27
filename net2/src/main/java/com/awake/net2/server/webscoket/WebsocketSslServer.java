package com.awake.net2.server.webscoket;

import com.awake.net2.handler.ServerRouterHandler;
import com.awake.net2.handler.codec.WebSocketCodecHandler;
import com.awake.net2.handler.idle.ServerIdleHandler;
import com.awake.net2.server.AbstractServer;
import com.awake.util.IOUtils;
import com.awake.util.net.HostAndPort;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLException;
import java.io.InputStream;

/**
 * @Author：lqh
 * @Date：2024/3/27 14:38
 */
public class WebsocketSslServer extends AbstractServer<SocketChannel> {

    private SslContext sslContext;

    public WebsocketSslServer(HostAndPort host, InputStream pem, InputStream key) {
        super(host);
        try {
            this.sslContext = SslContextBuilder.forServer(pem, key).build();
        } catch (SSLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new IdleStateHandler(0, 0, 180));
        channel.pipeline().addLast(new ServerIdleHandler());
        channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));
        channel.pipeline().addLast(new HttpServerCodec(8 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB));
        channel.pipeline().addLast(new HttpObjectAggregator(16 * IOUtils.BYTES_PER_MB));
        channel.pipeline().addLast(new WebSocketServerProtocolHandler("/"));
        channel.pipeline().addLast(new ChunkedWriteHandler());
        channel.pipeline().addLast(new WebSocketCodecHandler());
        channel.pipeline().addLast(new ServerRouterHandler());
    }

}

