package com.awake.net.gateway.core.handler;

import com.awake.NetContext;
import com.awake.event.manger.EventBus;
import com.awake.gateway.core.IGatewayLoadBalancer;
import com.awake.gateway.core.event.GatewaySessionInactiveEvent;
import com.awake.net.packet.DecodedPacketInfo;
import com.awake.net.packet.common.Heartbeat;
import com.awake.net.packet.common.Ping;
import com.awake.net.packet.common.Pong;
import com.awake.net.consumer.balancer.ConsistentHashConsumerLoadBalancer;
import com.awake.net.router.attachment.GatewayAttachment;
import com.awake.net.router.attachment.SignalAttachment;
import com.awake.net.router.handler.ServerRouteHandler;
import com.awake.net.session.Session;
import com.awake.net.util.SessionUtils;
import com.awake.util.JsonUtils;
import com.awake.util.base.StringUtils;
import com.awake.util.time.TimeUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @version : 1.0
 * @ClassName: GatewayRouteHandler
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 18:11
 **/

@ChannelHandler.Sharable
public class GatewayRouteHandler extends ServerRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(GatewayRouteHandler.class);

    public static final BiFunction<Session, Object, Boolean> DEFAULT_PACKER_FILTER = (session, packet) -> Boolean.FALSE;

    private final BiFunction<Session, Object, Boolean> packetFilter;

    public GatewayRouteHandler(@Nullable BiFunction<Session, Object, Boolean> packetFilter) {
        this.packetFilter = Objects.requireNonNullElse(packetFilter, DEFAULT_PACKER_FILTER);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 请求者的session，一般是serverSession
        var session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }

        var decodedPacketInfo = (DecodedPacketInfo) msg;
        var packet = decodedPacketInfo.getPacket();
        if (packet.getClass() == Heartbeat.class) {
            return;
        }
        if (packet.getClass() == Ping.class) {
            NetContext.getRouter().send(session, Pong.valueOf(TimeUtils.now()), null);
            return;
        }

        // 过滤非法包
        if (packetFilter.apply(session, packet)) {
            throw new IllegalArgumentException(StringUtils.format(" session:{}发送了一个非法包[{}]", SessionUtils.sessionSimpleInfo(ctx), JsonUtils.object2String(packet)));
        }


        // 把客户端信息包装为一个GatewayAttachment,因此通过这个网关附加包可以得到玩家的uid、sid之类的信息
        var gatewayAttachment = new GatewayAttachment(session);
        var signalAttachment = (SignalAttachment) decodedPacketInfo.getAttachment();
        if (signalAttachment != null) {
            signalAttachment.setClient(SignalAttachment.SIGNAL_SERVER);
            gatewayAttachment.setSignalAttachment(signalAttachment);
        }

        // 网关优先使用IGatewayLoadBalancer作为一致性hash的计算参数，然后才会使用客户端的session做参数
        // 例子：以聊天服务来说，玩家知道自己在哪个群组groupId中，那往这个群发送消息时，会在Packet中带上这个groupId做为一致性hash就可以了。
        if (packet instanceof IGatewayLoadBalancer) {
            var loadBalancerConsistentHashObject = ((IGatewayLoadBalancer) packet).loadBalancerConsistentHashObject();
            gatewayAttachment.setTaskExecutorHash(loadBalancerConsistentHashObject.hashCode());
            forwardingPacket(packet, gatewayAttachment, loadBalancerConsistentHashObject);
            return;
        } else {
            // 使用用户的uid做一致性hash
            var uid = session.getUid();
            if (uid > 0) {
                forwardingPacket(packet, gatewayAttachment, uid);
                return;
            }
        }
        // 再使用session的sid做一致性hash，因为每次客户端连接过来sid都会改变，所以客户端重新建立连接的话可能会被路由到其它的服务器
        // 如果有特殊需求的话，可以考虑去重写网关的转发策略
        // 拿着玩家的sid做一致性hash，那肯定是：一旦重连sid就会一直变化。所以：一般情况下除非自己创建TcpClient，否则逻辑不应该走到这里。 而是走上面的通过UID做一致性hash
        var sid = session.getSid();
        forwardingPacket(packet, gatewayAttachment, sid);
    }

    /**
     * 转发网关收到的包到Provider
     */
    private void forwardingPacket(Object packet, Object attachment, Object argument) {
        try {
            var consumerSession = ConsistentHashConsumerLoadBalancer.getInstance().loadBalancer(packet, argument);
            NetContext.getRouter().send(consumerSession, packet, attachment);
        } catch (Exception e) {
            logger.error("An exception occurred at the gateway", e);
        } catch (Throwable t) {
            logger.error("An error occurred at the gateway", t);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        var session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }

        var sid = session.getSid();
        var uid = session.getUid();

        // 连接到网关的客户端断开了连接
        EventBus.publicEvent(GatewaySessionInactiveEvent.valueOf(sid, uid));

        super.channelInactive(ctx);
    }
}
