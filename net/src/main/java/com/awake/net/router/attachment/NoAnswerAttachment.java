package com.awake.net.router.attachment;

import com.awake.net.packet.common.AttachmentType;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: NoAnswerAttachment
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/8 20:04
 **/
@Data
@ProtobufClass
public class NoAnswerAttachment implements IAttachment {
    @Ignore
    public static final int PROTOCOL_ID = 5;

    private int taskExecutorHash;

    public static NoAnswerAttachment valueOf(int taskExecutorHash) {
        NoAnswerAttachment attachment = new NoAnswerAttachment();
        attachment.taskExecutorHash = taskExecutorHash;
        return attachment;
    }

    @Override
    public AttachmentType packetType() {
        return AttachmentType.NO_ANSWER_PACKET;
    }

    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }

    public int getTaskExecutorHash() {
        return taskExecutorHash;
    }

    public void setTaskExecutorHash(int taskExecutorHash) {
        this.taskExecutorHash = taskExecutorHash;
    }
}
