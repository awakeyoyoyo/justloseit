package com.awake.net.packet;

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * @version : 1.0
 * @ClassName: EncodePacketInfo
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/3 19:44
 **/
@Data
public class EncodedPacketInfo{

    /**
     * 解码后的包
     */
    private IPacket packet;

    /**
     * 解码后的包的附加包
     */
    private IAttachment attachment;

    /**
     * 长度
     */
    private int length;
    /**
     * 加密所用时间
     */
    private long encodedTime;

    public static EncodedPacketInfo valueOf(IPacket packet, @Nullable IAttachment attachment) {
        EncodedPacketInfo packetInfo = new EncodedPacketInfo();
        packetInfo.packet = packet;
        packetInfo.attachment = attachment;
        return packetInfo;
    }
}
