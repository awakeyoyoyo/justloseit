package com.awake.game.config;


import com.awake.NetContext;
import com.awake.event.EventContext;
import com.awake.net.config.model.NetConfig;
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
    private EventContext eventContext;
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(netContext.toString());
        System.out.println(config.getProtocolConfig().getScanProtocolPacket());
    }
}
