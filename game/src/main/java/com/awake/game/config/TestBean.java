package com.awake.game.config;


import com.awake.NetContext;
import com.awake.net.config.model.NetConfig;
import com.awake.net.protocol.ProtocolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestBean implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(TestBean.class);
    @Autowired
    private NetConfig config;
    @Autowired
    private NetContext netContext;
    @Autowired
    private ProtocolManager protocolManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("11111111111111111111111111111111111111");
        System.out.println(config.getZookeeperConfig());
        System.out.println(protocolManager.getProtocolProperties());
        System.out.println(netContext.getProtocolManager());
    }
}
