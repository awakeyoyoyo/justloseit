package com.awake.net.router.task;

import com.awake.NetContext;
import com.awake.net.packet.IPacket;
import com.awake.net.router.attachment.IAttachment;
import com.awake.net.session.Session;

/**
 * @version : 1.0
 * @ClassName: PacketReceiverTask
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/6 11:06
 **/
public class PacketReceiverTask implements Runnable{
    private Session session;
    private IPacket packet;
    private IAttachment attachment;

    public PacketReceiverTask(Session session, IPacket packet, IAttachment attachment) {
        this.session = session;
        this.packet = packet;
        this.attachment = attachment;
    }

    @Override
    public void run() {
        NetContext.getRouter().atReceiver(session, packet, attachment);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public IPacket getPacket() {
        return packet;
    }

    public void setPacket(IPacket packet) {
        this.packet = packet;
    }

    public IAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(IAttachment attachment) {
        this.attachment = attachment;
    }
}
