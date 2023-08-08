package com.awake.net.router.attachment;

import com.awake.net.packet.IPacket;
import com.awake.net.packet.common.AttachmentType;

/**
 * @version : 1.0
 * @ClassName: IAttachment
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/8 19:59
 **/
public interface IAttachment extends IPacket {

    AttachmentType packetType();
}
