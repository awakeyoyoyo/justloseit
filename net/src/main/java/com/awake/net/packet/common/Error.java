package com.awake.net.packet.common;

import com.awake.NetContext;
import com.awake.net.packet.IPacket;
import com.awake.net.protocol.anno.Packet;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @version : 1.0
 * @ClassName: Error
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 17:16
 **/
@Packet(protocolId = Error.PROTOCOL_ID, moduleId = ModuleConstant.COMMON_MODULE_ID)
@ProtobufClass
@Data
public class Error implements IPacket {
    @Ignore
    public static final int PROTOCOL_ID = 101;

    private int module;

    private int errorCode;

    private String errorMessage;

    @Override
    public int protocolId() {
        return PROTOCOL_ID;
    }

    public static short errorProtocolId() {
        return PROTOCOL_ID;
    }

    @Override
    public String toString() {
        FormattingTuple message = MessageFormatter.arrayFormat(
                "module:[{}], errorCode:[{}], errorMessage:[{}]", new Object[]{module, errorCode, errorMessage});
        return message.getMessage();
    }

    public static Error valueOf(int module, int errorCode, String errorMessage) {
        Error response = new Error();
        response.module = module;
        response.errorCode = errorCode;
        response.errorMessage = errorMessage;
        return response;
    }

    public static Error valueOf(IPacket packet, int errorCode, String errorMessage) {
        Error response = new Error();
        response.module = NetContext.getProtocolManager().getProtocolDefinition(packet.protocolId()).getModule();
        response.errorCode = errorCode;
        response.errorMessage = errorMessage;
        return response;
    }

    public static Error valueOf(IPacket packet, int errorCode) {
        return valueOf(packet, errorCode, null);
    }

    public static Error valueOf(IPacket packet, String errorMessage) {
        return valueOf(packet, 0, errorMessage);
    }

    public static Error valueOf(String errorMessage) {
        return valueOf(0, 0, errorMessage);
    }

}

