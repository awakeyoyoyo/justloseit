package com.awake.net.rpc.balancer;

import com.awake.NetContext;
import com.awake.exception.RunException;
import com.awake.model.Pair;
import com.awake.net.config.model.ProtocolModule;
import com.awake.net.protocol.ProtocolManager;
import com.awake.net.session.Session;
import com.awake.net.util.ConsistentHash;
import com.awake.net.util.FastTreeMapIntLong;
import com.awake.util.base.CollectionUtils;
import org.springframework.lang.Nullable;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.Collectors;

/**
 * @version : 1.0
 * @ClassName: ConsistentHashConsumerLoadBalancer
 * @Description: 哈希一致性选择消费者
 * @Auther: awake
 * @Date: 2023/10/11 19:34
 **/
public class ConsistentHashConsumerLoadBalancer extends AbstractConsumerLoadBalancer {

    public static final ConsistentHashConsumerLoadBalancer INSTANCE = new ConsistentHashConsumerLoadBalancer();


    private volatile int lastClientSessionChangeId = 0;
    private static final AtomicReferenceArray<FastTreeMapIntLong> consistentHashMap = new AtomicReferenceArray<>(ProtocolManager.MAX_MODULE_NUM);
    private static final int VIRTUAL_NODE_NUMS = 200;

    private ConsistentHashConsumerLoadBalancer() {
    }

    public static ConsistentHashConsumerLoadBalancer getInstance() {
        return INSTANCE;
    }

    /**
     * 通过argument的toString计算一致性hash，所以传入的argument一般要能代表唯一性，比如用户的id
     *
     * @param packet   请求包
     * @param argument 参数，一般要能代表唯一性，比如用户的id
     * @return 调用的session
     */
    @Override
    public Session loadBalancer(Object packet, Object argument) {
        if (argument == null) {
            return RandomConsumerLoadBalancer.getInstance().loadBalancer(packet, argument);
        }

        // 如果更新时间不匹配，则更新到最新的服务提供者
        var currentClientSessionChangeId = NetContext.getSessionManager().getClientSessionChangeId();
        if (currentClientSessionChangeId != lastClientSessionChangeId) {
            for (byte i = 0; i < ProtocolManager.MAX_MODULE_NUM; i++) {
                var consistentHash = consistentHashMap.get(i);
                if (consistentHash == null) {
                    continue;
                }
                var module = ProtocolManager.moduleByModuleId(i);
                updateModuleToConsistentHash(module);
            }
            lastClientSessionChangeId = currentClientSessionChangeId;
        }

        var module = ProtocolManager.moduleByProtocol(packet.getClass());
        var fastTreeMap = consistentHashMap.get(module.getId());
        if (fastTreeMap == null) {
            fastTreeMap = updateModuleToConsistentHash(module);
        }
        if (fastTreeMap == null) {
            throw new RunException("ConsistentHashLoadBalancer [protocol:{}][argument:{}], no service provides the [module:{}]", packet.getClass(), argument, module);
        }
        var nearestIndex = fastTreeMap.indexOfNearestCeilingKey(argument.hashCode());
        if (nearestIndex < 0) {
            throw new RunException("no service provides the [module:{}]", module);
        }
        var sid = fastTreeMap.getByIndex(nearestIndex);
        var session = NetContext.getSessionManager().getClientSession(sid);
        if (session == null) {
            throw new RunException("unknown no service provides the [module:{}]", module);
        }
        return session;
    }

    @Nullable
    private FastTreeMapIntLong updateModuleToConsistentHash(ProtocolModule module) {
        var sessionStringList = getSessionsByModule(module).stream()
                .map(session -> new Pair<>(session.getProviderAttribute().toString(), session.getSid()))
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .collect(Collectors.toList());

        var consistentHash = new ConsistentHash<>(sessionStringList, VIRTUAL_NODE_NUMS);
        var virtualNodeTreeMap = consistentHash.getVirtualNodeTreeMap();
        if (CollectionUtils.isEmpty(virtualNodeTreeMap)) {
            consistentHashMap.set(module.getId(), null);
            return null;
        }
        var virtualTreeMap = new TreeMap<Integer, Long>();
        for (var entry : virtualNodeTreeMap.entrySet()) {
            virtualTreeMap.put(entry.getKey(), entry.getValue().getValue());
        }
        // 使用更高性能的tree map
        var fastTreeMap = new FastTreeMapIntLong(virtualTreeMap);
        consistentHashMap.set(module.getId(), fastTreeMap);
        return fastTreeMap;
    }
}
