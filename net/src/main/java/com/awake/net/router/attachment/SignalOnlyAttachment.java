package com.awake.net.router.attachment;

import com.awake.net.packet.common.AttachmentType;
import com.awake.net.packet.common.ModuleConstant;
import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: SignalOnlyAttachment
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/6 11:44
 **/
@Packet(protocolId = SignalOnlyAttachment.PROTOCOL_ID, moduleId = ModuleConstant.COMMON_MODULE_ID)
@ProtobufClass
@Data
public class SignalOnlyAttachment implements IAttachment {
    @Ignore
    public static final short PROTOCOL_ID = 2;

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
