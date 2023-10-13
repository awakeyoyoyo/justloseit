package com.awake.net.provider.registry;

import com.awake.net.config.model.ConsumerProperties;
import com.awake.net.config.model.ProviderProperties;
import com.awake.util.IdUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version : 1.0
 * @ClassName: RegisterVo
 * @Description: 注册vo
 * @Auther: awake
 * @Date: 2023/7/31 10:29
 **/
@Data
public class RegisterVO {

    private static final Logger logger = LoggerFactory.getLogger(RegisterVO.class);

    private static final String uuid = IdUtils.getUUID();

    private String id;

    // 服务提供者配置
    private ProviderProperties providerProperties;
    // 服务消费者配置
    private ConsumerProperties consumerProperties;

}
