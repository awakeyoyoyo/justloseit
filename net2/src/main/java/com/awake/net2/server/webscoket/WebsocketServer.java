package com.awake.net2.server.webscoket;


import com.awake.net2.handler.ServerRouterHandler;
import com.awake.net2.handler.codec.WebSocketCodecHandler;
import com.awake.net2.server.AbstractServer;
import com.awake.util.IOUtils;
import com.awake.util.net.HostAndPort;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author：lqh
 * @Date：2024/3/27 14:38
 */
public class WebsocketServer extends AbstractServer<SocketChannel> {

    public WebsocketServer(HostAndPort host) {
        super(host);
    }

    @Override
    public void initChannel(SocketChannel channel) {
        // 编解码 http 请求
        channel.pipeline().addLast(new HttpServerCodec(8 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB));
        // 聚合解码 HttpRequest/HttpContent/LastHttpContent 到 FullHttpRequest
        // 保证接收的 Http 请求的完整性
        channel.pipeline().addLast(new HttpObjectAggregator(16 * IOUtils.BYTES_PER_MB));
        // 处理其他的 WebSocketFrame
        channel.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
        // 写文件内容，支持异步发送大的码流，一般用于发送文件流
        channel.pipeline().addLast(new ChunkedWriteHandler());
        // 编解码WebSocketFrame二进制协议
        channel.pipeline().addLast(new WebSocketCodecHandler());
        channel.pipeline().addLast(new ServerRouterHandler());
    }

}

