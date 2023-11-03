package com.awake.net.util;

import com.awake.NetContext;
import com.awake.net.session.Session;
import com.awake.util.FileUtils;
import com.awake.util.base.StringUtils;
import com.awake.util.base.ThreadUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: SessionUtils
 * @Description: 操作session工具类
 * @Auther: awake
 * @Date: 2023/7/31 11:12
 **/
public class SessionUtils {
    public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");

    private static final String CHANNEL_INFO_TEMPLATE = "[ip:{}][sid:{}][uid:{}]";

    private static final String CHANNEL_SIMPLE_INFO_TEMPLATE = "[sid:{}][uid:{}]";

    private static final String CHANNEL_TEMPLATE = "[channel:{}]";

    public static boolean isActive(Session session) {
        return session != null && session.getChannel().isActive();
    }

    public static boolean isActive(Channel session) {
        return session != null && session.isActive();
    }

    public static Session getSession(ChannelHandlerContext ctx) {
        Attribute<Session> sessionAttr = ctx.channel().attr(SESSION_KEY);
        return sessionAttr.get();
    }

    public static String sessionInfo(ChannelHandlerContext ctx) {
        Session session = SessionUtils.getSession(ctx);
        if (session == null) {
            return StringUtils.format(CHANNEL_TEMPLATE, ctx.channel());
        }
        return sessionInfo(session);
    }

    public static String sessionInfo(Session session) {
        if (session == null) {
            return CHANNEL_INFO_TEMPLATE;
        }
        String remoteAddress = StringUtils.EMPTY;
        try {
            remoteAddress = StringUtils.substringAfterFirst(session.getChannel().remoteAddress().toString(), StringUtils.SLASH);
        } catch (Throwable t) {
            // do nothing
            // to avoid: io.netty.channel.unix.Errors$NativeIoException: readAddress(..) failed: Connection reset by peer
            // 有些情况当建立连接过后迅速关闭，这个时候取remoteAddress会有异常
        }
        return StringUtils.format(CHANNEL_INFO_TEMPLATE, remoteAddress, session.getSid(), session.getUid());
    }

    public static String sessionSimpleInfo(ChannelHandlerContext ctx) {
        Session session = SessionUtils.getSession(ctx);
        if (session == null) {
            return StringUtils.format(CHANNEL_TEMPLATE, ctx.channel());
        }
        return sessionSimpleInfo(session);
    }

    public static String sessionSimpleInfo(Session session) {
        if (session == null) {
            return CHANNEL_SIMPLE_INFO_TEMPLATE;
        }
        return StringUtils.format(CHANNEL_SIMPLE_INFO_TEMPLATE, session.getSid(), session.getUid());
    }

    public static Session initChannel(Channel channel) {
        Attribute<Session> sessionAttr = channel.attr(SESSION_KEY);
        Session session = new Session(channel);
        boolean setSuccessful = sessionAttr.compareAndSet(null, session);
        if (!setSuccessful) {
            channel.close();
            throw new RuntimeException(StringUtils.format("The properties of the session[channel:{}] cannot be set", channel));
        }
        return session;
    }

    public static void printSessionInfo() {
        Thread thread = new Thread(() -> {
            while (true) {
                ThreadUtils.sleep(10_000);
                var builder = new StringBuilder();
                builder.append(FileUtils.LS);
                NetContext.getSessionManager().forEachClientSession(new Consumer<Session>() {
                    @Override
                    public void accept(Session session) {
                        builder.append(StringUtils.format("[session:{}，sid:{}]", session.getChannel().remoteAddress(),session.getSid()));
                        builder.append(FileUtils.LS);
                    }
                });

                builder.append(StringUtils.format("serverSession count：[{}]", NetContext.getSessionManager().serverSessionSize()));
                builder.append(FileUtils.LS);
                NetContext.getSessionManager().forEachServerSession(new Consumer<Session>() {
                    @Override
                    public void accept(Session session) {
                        builder.append(StringUtils.format("[session:{}]", session.getChannel().remoteAddress()));
                        builder.append(FileUtils.LS);
                    }
                });

                System.out.println(builder.toString());

            }
        });
        thread.start();
    }
}
