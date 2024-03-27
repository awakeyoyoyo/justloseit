package com.awake.net2.server.webscoket;


import com.awake.net2.handler.ClientRouterHandler;
import com.awake.net2.handler.codec.WebSocketCodecHandler;
import com.awake.net2.server.AbstractClient;
import com.awake.util.IOUtils;
import com.awake.util.net.HostAndPort;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author：lqh
 * @Date：2024/3/27 14:37
 */
public class WebsocketClient extends AbstractClient<SocketChannel> {

    private final WebSocketClientProtocolConfig webSocketClientProtocolConfig;

    public WebsocketClient(HostAndPort host, WebSocketClientProtocolConfig webSocketClientProtocolConfig) {
        super(host);
        this.webSocketClientProtocolConfig = webSocketClientProtocolConfig;
    }

    @Override
    public void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new HttpClientCodec(8 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB));
        channel.pipeline().addLast(new HttpObjectAggregator(16 * IOUtils.BYTES_PER_MB));
        channel.pipeline().addLast(new WebSocketClientProtocolHandler(webSocketClientProtocolConfig));
        channel.pipeline().addLast(new ChunkedWriteHandler());
        channel.pipeline().addLast(new WebSocketCodecHandler());
        channel.pipeline().addLast(new ClientRouterHandler());
    }
}
