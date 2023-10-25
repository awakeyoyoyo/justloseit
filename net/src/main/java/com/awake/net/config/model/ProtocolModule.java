package com.awake.net.config.model;

import com.awake.util.base.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version : 1.0
 * @ClassName: ProtocolModule
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/1 10:54
 **/
@Data
@NoArgsConstructor
public class ProtocolModule {

    public static final ProtocolModule DEFAULT_PROTOCOL_MODULE = new ProtocolModule((byte) 0, "default");

    private int id;

    private String name;

    public ProtocolModule(int id, String name) {
        if (id < 0) {
            throw new IllegalArgumentException(StringUtils.format("模块[{}]的id[{}]必须大于0", name, id));
        }

        this.id = id;
        this.name = name;
    }
}
