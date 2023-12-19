package com.awake.net.rpc.balancer;

import com.awake.NetContext;
import com.awake.net.config.model.ProtocolModule;
import com.awake.net.router.PacketManager;
import com.awake.net.rpc.registry.RegisterVO;
import com.awake.net.session.Session;
import com.awake.util.base.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: AbstractConsumerLoadBalancer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/11 18:20
 **/
public abstract class AbstractConsumerLoadBalancer implements IConsumerLoadBalancer {

    public static AbstractConsumerLoadBalancer valueOf(String loadBalancer) {
        AbstractConsumerLoadBalancer balancer;
        switch (loadBalancer) {
            case "random":
                balancer = RandomConsumerLoadBalancer.getInstance();
                break;
            case "consistent-hash":
                balancer = ConsistentHashConsumerLoadBalancer.getInstance();
                break;
            default:
                throw new RuntimeException(StringUtils.format("Load balancer is not recognized[{}]", loadBalancer));
        }
        return balancer;
    }

    public List<Session> getSessionsByModule(ProtocolModule module) {
        var list = new ArrayList<Session>();
        NetContext.getSessionManager().forEachClientSession(new Consumer<Session>() {
            @Override
            public void accept(Session session) {
                if (session.getProviderAttribute() == null || session.getProviderAttribute().getProviderConfig() == null) {
                    return;
                }
                var providerConfig = session.getProviderAttribute().getProviderConfig();
                if (providerConfig.getProviders().stream().anyMatch(it -> it.getProtocolModule().equals(module))) {
                    list.add(session);
                }
            }
        });
        return list;
    }

    public boolean sessionHasModule(Session session, Object packet) {
        var consumerAttribute = session.getProviderAttribute();
        if (Objects.isNull(consumerAttribute)) {
            return false;
        }

        var registerVO = (RegisterVO) consumerAttribute;
        if (Objects.isNull(registerVO.getConsumerConfig())) {
            return false;
        }

        var module = PacketManager.moduleByProtocol(packet.getClass());
        return registerVO.getProviderConfig().getProviders().contains(module);
    }
}
