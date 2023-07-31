package com.awake.net.config.util;

import com.awake.net.config.session.Session;
import com.awake.util.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

import static com.awake.net.config.session.Session.SESSION_KEY;

/**
 * @version : 1.0
 * @ClassName: SessionUtils
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/31 11:12
 **/
public class SessionUtils {
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

}
