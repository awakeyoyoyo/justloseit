package com.awake.net2.session;

import com.awake.util.base.StringUtils;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author：lqh
 * @Date：2024/3/15 10:54
 */
@Data
public class Session implements Closeable {
    public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0);

    /**
     * The globally unique ID of the session and the negative sid are allowed
     */
    private long sessionId = ATOMIC_LONG.incrementAndGet();

    private Channel channel;

    /**
     * 默认0，登陆后才进行赋值
     */
    private long userId = 0;

    public Session(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel cannot be empty");
        }
        this.channel = channel;
    }

    @Override
    public void close() {
        channel.close();
    }

    public static Session valueOf(Channel channel) {
        var sessionAttr = channel.attr(SESSION_KEY);
        var session = new Session(channel);
        var setSuccessful = sessionAttr.compareAndSet(null, session);
        if (!setSuccessful) {
            channel.close();
            throw new RuntimeException(StringUtils.format("The properties of the session[channel:{}] cannot be set", channel));
        }
        return session;
    }
}
