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
    private Object packet;

    /**
     * 解码后的包的附加包
     */
    private Object attachment;


    public static EncodedPacketInfo valueOf(Object packet, @Nullable Object attachment) {
        EncodedPacketInfo packetInfo = new EncodedPacketInfo();
        packetInfo.packet = packet;
        packetInfo.attachment = attachment;
        return packetInfo;
    }
}
