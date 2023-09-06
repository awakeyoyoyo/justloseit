package com.awake.net.packet.common;

import com.awake.net.router.attachment.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: AttachmentType
 * @Description: 附加包类型
 * @Auther: awake
 * @Date: 2023/8/3 19:43
 **/
public enum AttachmentType {
    /**
     * synchronous or asynchronous attachment
     */
    SIGNAL_PACKET( 0, SignalAttachment.class),

    SIGNAL_ONLY_PACKET((byte) 1, SignalOnlyAttachment.class),

    /**
     * gateway attachment
     */
    GATEWAY_PACKET((byte) 2, GatewayAttachment.class),

    /**
     * not used attachment
     */
    NO_ANSWER_PACKET( 5, NoAnswerAttachment.class),
    ;


    public static final Map<Integer, AttachmentType> map = new HashMap<>(values().length);

    static {
        for (AttachmentType packetType : AttachmentType.values()) {
            map.put(packetType.packetType, packetType);
        }
    }

    private int packetType;

    public int getPacketType() {
        return packetType;
    }

    private Class<? extends IAttachment> clazz;

    AttachmentType(int packetType, Class<? extends IAttachment> clazz) {
        this.packetType = packetType;
        this.clazz = clazz;
    }

    public static AttachmentType getPacketType(int packetType) {
        return map.getOrDefault(packetType, AttachmentType.NO_ANSWER_PACKET);
    }
}
