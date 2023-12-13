package com.awake.net.packet;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: DecodedPacketInfo
 * @Description: 解码结果
 * @Auther: awake
 * @Date: 2023/8/3 19:41
 **/
@Data
public class DecodedPacketInfo {

    /**
     * 解码后的包
     */
    private Object packet;

    /**
     * 解码后的包的附加包
     */
    private Object attachment;;

    public static DecodedPacketInfo valueOf(Object packet, Object attachment) {
        DecodedPacketInfo packetInfo = new DecodedPacketInfo();
        packetInfo.packet = packet;
        packetInfo.attachment = attachment;
        return packetInfo;
    }
}
