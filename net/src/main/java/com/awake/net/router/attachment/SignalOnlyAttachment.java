package com.awake.net.router.attachment;

import com.awake.net.packet.common.AttachmentType;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: SignalOnlyAttachment
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/6 11:44
 **/
@Data
public class SignalOnlyAttachment implements IAttachment {

    public static final short PROTOCOL_ID = 1;

    private int signalId;

    private long timestamp;

    @Override
    public AttachmentType packetType() {
        return AttachmentType.SIGNAL_ONLY_PACKET;
    }

    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }
}
