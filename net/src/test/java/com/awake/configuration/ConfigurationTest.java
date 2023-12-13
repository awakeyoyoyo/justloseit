package com.awake.configuration;


import com.awake.net.autoconfigure.NetAutoConfiguration;
import com.awake.net.config.ConfigManager;
import com.awake.net.config.model.ConsumerProperties;
import com.awake.net.config.model.NetConfig;
import com.awake.net.config.model.ProviderProperties;
import com.awake.net.protocol.ProtocolManager;
import com.awake.net.rpc.registry.RegisterVO;
import io.netty.util.NetUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: ConfigurationTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/13 17:23
 **/

@SpringBootTest(classes = {NetAutoConfiguration.class})
public class ConfigurationTest {

    @Autowired
    private NetConfig netConfig;
    @Autowired
    private ProtocolManager protocolManager;
    @Autowired
    private ConfigManager configManager;
    @Test
    public void configurationTest() {
        System.out.println(netConfig);
        System.out.println(protocolManager);
        System.out.println(configManager);
    }

    @Test
    public void registerVoTest() {
        // 服务提供者模块列表和服务提供者配置
        // 定义2个服务提供者模块
        ProviderProperties provider = netConfig.getProvider();
        ConsumerProperties consumer = netConfig.getConsumer();
        var vo =  RegisterVO.valueOf(provider, consumer);
        var voStr = vo.toString();

        // test | 127.0.0.1:80 | provider:[100-aaa-a, 120-bbb-b] | consumer:[100-aaa-random-a, 120-bbb-random-b]
        System.out.println(voStr);

        var newVo = RegisterVO.parseString(voStr);
        System.out.println(newVo.toString());
////        Assert.assertEquals(vo, newVo);

        // /127.0.0.1
        System.out.println(NetUtil.LOCALHOST);

        // localhost/127.0.0.1
        System.out.println(NetUtil.LOCALHOST4);

        //localhost/0:0:0:0:0:0:0:1
        System.out.println(NetUtil.LOCALHOST6);

        // 200
        System.out.println(NetUtil.SOMAXCONN);

        // name:lo (Software Loopback Interface 1)
        System.out.println(NetUtil.LOOPBACK_IF);
    }
}
