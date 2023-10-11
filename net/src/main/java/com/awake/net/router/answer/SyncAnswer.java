package com.awake.net.router.answer;

import com.awake.net.packet.IPacket;
import com.awake.net.router.attachment.SignalAttachment;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: SyncAnswer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/8 20:16
 **/
@Data
public class SyncAnswer <T> implements ISyncAnswer<T> {

    private final T packet;

    private final SignalAttachment attachment;

    public SyncAnswer(T packet, SignalAttachment attachment) {
        this.packet = packet;
        this.attachment = attachment;
    }

    @Override
    public T packet() {
        return packet;
    }

    @Override
    public SignalAttachment attachment() {
        return attachment;
    }
}
