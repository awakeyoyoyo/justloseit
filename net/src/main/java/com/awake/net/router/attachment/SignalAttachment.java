package com.awake.net.router.attachment;

import com.awake.net.packet.IPacket;
import com.awake.net.packet.common.AttachmentType;
import com.awake.util.TimeUtils;
import lombok.Data;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version : 1.0
 * @ClassName: SignalAttachment
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/8 20:03
 **/
@Data
public class SignalAttachment implements IAttachment {
    public static final short PROTOCOL_ID = 0;

    /**
     * 允许负数的signalId
     */
    public static final AtomicInteger ATOMIC_ID = new AtomicInteger(0);

    /**
     * 唯一标识一个packet， 唯一表示一个Attachment，hashcode() and equals() 等于signalId
     */
    private int signalId = ATOMIC_ID.incrementAndGet();

    /**
     * 0 for the server, 1 for the sync or async native client, 2 for the outside client such as browser, mobile
     */
    public static final byte SIGNAL_SERVER = 0;
    public static final byte SIGNAL_NATIVE_ARGUMENT_CLIENT = 1;
    public static final byte SIGNAL_NATIVE_NO_ARGUMENT_CLIENT = 2;
    public static final byte SIGNAL_OUTSIDE_CLIENT = 12;


    /**
     * 用来在TaskBus中计算hash的参数，用来决定任务在哪一条线程执行
     */
    private int taskExecutorHash = -1;

    /**
     * 客户端标识，false为服务端返回的包
     */
    private byte client = SIGNAL_NATIVE_ARGUMENT_CLIENT;

    /**
     * 客户端发送时间
     */
    private long timestamp = TimeUtils.now();

    /**
     * 客户端收到服务器回复的时候回调的方法
     */
    private transient CompletableFuture<IPacket> responseFuture = new CompletableFuture<>();

    public SignalAttachment() {
    }


    @Override
    public AttachmentType packetType() {
        return AttachmentType.SIGNAL_PACKET;
    }

    /**
     * 用来确定这条消息在哪一个线程处理
     */
    public int taskExecutorHash() {
        return taskExecutorHash;
    }

    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }

}
