package com.awake.net.config.model;

import lombok.Data;

/**
 * @version : 1.0
 * @ClassName: ProviderModule
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/1 10:39
 **/

@Data
public class ProviderModule {

    /**
     * 模块id和模块名
     */
    private ProtocolModule protocolModule;

    /**
     * 提供者名字
     */
    private String provider;

    public ProviderModule(ProtocolModule protocolModule, String provider) {
        this.protocolModule = protocolModule;
        this.provider = provider;
    }
}
