package com.awake.net.router.attachment;

import com.awake.net.packet.common.AttachmentType;
import com.awake.net.session.Session;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: GatewayAttachment
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/6 11:43
 **/
@ProtobufClass
@Data
public class GatewayAttachment implements IAttachment {
    @Ignore
    public static final short PROTOCOL_ID = 3;

    /**
     * session id
     */
    private long sid;

    /**
     * EN:User ID, the message forwarded from the gateway to the back must be accompanied by the user's ID information,
     * otherwise it is impossible to know which user sent it, 0 means no user ID
     * <p>
     * CN:用戶Id，从网关转发到后面的消息必须要附带用户的Id信息，要不然无法知道是哪个用户发过来的，0代表没有用户id
     */
    private long uid;

    /**
     * EN:Used to determine which thread the message is processed on
     * CN:用来确定这条消息在哪一个线程处理
     */
    private int taskExecutorHash;

    /**
     * true for the client, false for the server
     */
    private boolean client;

    /**
     * EN:The client may send an packet with synchronous or asynchronous to the gateway, and the gateway needs to bring this attachment when forwarding
     * CN:客户端发到网关的可能是一个带有同步或者异步的附加包，网关转发的时候需要把这个附加包给带上
     */
    private SignalAttachment signalAttachment;


    public GatewayAttachment() {
    }

    public GatewayAttachment(Session session) {
        this.client = true;
        this.sid = session.getSid();
        this.uid = session.getUid();
    }

    public GatewayAttachment(long sid, long uid) {
        this.sid = sid;
        this.uid = uid;
    }


    @Override
    public AttachmentType packetType() {
        return AttachmentType.GATEWAY_PACKET;
    }

    /**
     * EN:Used to determine which thread the message is processed on
     * CN:用来确定这条消息在哪一个线程处理
     */
    public int taskExecutorHash() {
        return taskExecutorHash == 0 ? (int) uid : taskExecutorHash;
    }

    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }
}

