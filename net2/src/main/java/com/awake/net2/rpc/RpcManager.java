package com.awake.net2.rpc;

import com.awake.net2.rpc.properties.RpcProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author：lqh
 * @Date：2024/3/29 14:45
 */
public class RpcManager implements IRpcManager, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcManager.class);

    @Resource
    private RpcProperties rpcProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        //扫描注解
    }
}
