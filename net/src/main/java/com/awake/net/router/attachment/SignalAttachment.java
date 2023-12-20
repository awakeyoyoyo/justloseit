package com.awake.net.router.attachment;

import com.awake.net.packet.common.AttachmentType;
import com.awake.net.packet.common.ModuleConstant;
import com.awake.net.protocol.anno.Packet;
import com.awake.util.time.TimeUtils;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
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
@Packet(protocolId = SignalAttachment.PROTOCOL_ID,moduleId = ModuleConstant.COMMON_MODULE_ID)
@ProtobufClass
@Data
public class SignalAttachment implements IAttachment {
    @Ignore
    public static final short PROTOCOL_ID = 1;

    /**
     * 允许负数的signalId
     */
    @Ignore
    public static final AtomicInteger ATOMIC_ID = new AtomicInteger(0);

    /**
     * 唯一标识一个packet， 唯一表示一个Attachment，hashcode() and equals() 等于signalId
     */
    private int signalId = ATOMIC_ID.incrementAndGet();

    /**
     * 0 for the server, 1 for the sync or async native client, 2 for the outside client such as browser, mobile
     */
    @Ignore
    public static final int SIGNAL_SERVER = 0;
    @Ignore
    public static final int SIGNAL_NATIVE_ARGUMENT_CLIENT = 1;
    @Ignore
    public static final int SIGNAL_NATIVE_NO_ARGUMENT_CLIENT = 2;
    @Ignore
    public static final int SIGNAL_OUTSIDE_CLIENT = 12;


    /**
     * 用来在TaskBus中计算hash的参数，用来决定任务在哪一条线程执行
     */
    private int taskExecutorHash = -1;

    /**
     * 客户端标识，false为服务端返回的包
     */
    private int client = SIGNAL_NATIVE_ARGUMENT_CLIENT;

    /**
     * 客户端发送时间
     */
    private long timestamp = TimeUtils.now();

    /**
     * 客户端收到服务器回复的时候回调的方法
     */
    @Ignore
    private transient CompletableFuture<Object> responseFuture = new CompletableFuture<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignalAttachment that = (SignalAttachment) o;
        return signalId == that.signalId;
    }

    @Override
    public int hashCode() {
        return signalId;
    }

}
