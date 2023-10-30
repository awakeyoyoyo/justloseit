package com.awake.net.session;

import com.awake.net.rpc.registry.RegisterVO;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version : 1.0
 * @ClassName: Session
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/31 10:55
 **/
@Data
public class Session  implements Closeable {

    public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0);

    /**
     * The globally unique ID of the session and the negative sid are allowed
     */
    private long sid = ATOMIC_LONG.incrementAndGet();

    private Channel channel;

    // ------------------------------------------------------------------------------------------------------------
    // The following are extra parameters, add them yourself if necessary（下面都是额外参数，有需要的自己添加）
    /**
     * EN:The default user ID is an ID greater than 0, or equal 0 if there is no login, user extra parameters
     * CN:默认用户的id都是大于0的id，如果没有登录则等于0，用户额外参数
     */
    private long uid = 0;

    /**
     * EN:Session extra parameters
     * CN:Session附带的属性参数，连接上的服务提供者的属性
     */
    private RegisterVO providerAttribute = null;

    public Session(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel cannot be empty");
        }
        this.channel = channel;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
