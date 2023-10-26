package com.awake.net.consumer.registry;

import com.awake.net.config.model.*;
import com.awake.util.ExceptionUtils;
import com.awake.util.IdUtils;
import com.awake.util.base.CollectionUtils;
import com.awake.util.base.StringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private ProviderProperties providerConfig;
    // 服务消费者配置
    private ConsumerProperties consumerConfig;

    public static RegisterVO valueOf(ProviderProperties provider, ConsumerProperties consumer) {
        RegisterVO vo=new RegisterVO();
        vo.setId(provider.getId());
        vo.setProviderConfig(provider);
        vo.setConsumerConfig(consumer);
        return vo;
    }

    /**
     * 检测这个服务提供者 是否被传入的消费者消费
     * @param providerVO
     * @param consumerVO
     * @return
     */
    public static boolean providerHasConsumer(RegisterVO providerVO, RegisterVO consumerVO) {
        if (Objects.isNull(providerVO) || Objects.isNull(providerVO.providerConfig) || CollectionUtils.isEmpty(providerVO.providerConfig.getProviders())
                || Objects.isNull(consumerVO) || Objects.isNull(consumerVO.consumerConfig) || CollectionUtils.isEmpty(consumerVO.consumerConfig.getConsumers())) {
            return false;
        }
        for (var provider : providerVO.getProviderConfig().getProviders()) {
            if (consumerVO.getConsumerConfig().getConsumers().stream().anyMatch(it -> it.matchProvider(provider))) {
                return true;
            }
        }
        return false;
    }

    public String toProviderString() {
        return toString();
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        // 模块模块名
        builder.append(id);

        // 服务提供者相关配置信息
        if (Objects.nonNull(providerConfig)&&Objects.nonNull(providerConfig.getProviders())) {
            var providerAddress = providerConfig.getAddress();
            if (StringUtils.isBlank(providerAddress)) {
                throw new RuntimeException(StringUtils.format("providerConfig的address不能为空"));
            }
            builder.append(StringUtils.SPACE).append(StringUtils.VERTICAL_BAR).append(StringUtils.SPACE);
            // 服务提供者地址
            builder.append(providerAddress);

            builder.append(StringUtils.SPACE).append(StringUtils.VERTICAL_BAR).append(StringUtils.SPACE);
            var providerModules = providerConfig.getProviders().stream()
                    .map(it -> StringUtils.joinWith(StringUtils.HYPHEN, it.getProtocolModule().getId(), it.getProtocolModule().getName(), it.getProvider()))
                    .toArray();

            // 服务提供者模块信息列表
            builder.append(StringUtils.format("provider:[{}]"
                    , StringUtils.joinWith(StringUtils.COMMA + StringUtils.SPACE, providerModules)));
        }

        // 服务消费者相关信息
        if (Objects.nonNull(consumerConfig)&&(Objects.nonNull(consumerConfig.getConsumers()))) {
            builder.append(StringUtils.SPACE).append(StringUtils.VERTICAL_BAR).append(StringUtils.SPACE);

            var consumerModules = consumerConfig.getConsumers().stream()
                    .map(it -> StringUtils.joinWith(StringUtils.HYPHEN, it.getProtocolModule().getId(), it.getProtocolModule().getName(), it.getLoadBalancer(), it.getConsumer()))
                    .toArray();

            // 服务消费者模块信息列表
            builder.append(StringUtils.format("consumer:[{}]"
                    , StringUtils.joinWith(StringUtils.COMMA + StringUtils.SPACE, consumerModules)));
        }

        return builder.toString();
    }

    // tankHome | 192.168.3.2:12400 | provider:[3-tankHome-tankHomeProvider] | consumer:[4-tankCache-consistent-hash-tankCacheProvider]
    @Nullable
    public static RegisterVO parseString(String str) {
        try {
            var vo = new RegisterVO();
            var splits = str.split("\\|");

            vo.id = splits[0].trim();

            String providerAddress = null;

            for (var i = 1; i < splits.length; i++) {
                var s = splits[i].trim();
                if (s.startsWith("provider")) {
                    var providerModules = parseProviderModules(s);
                    vo.providerConfig = ProviderProperties.valueOf(providerAddress, providerModules);
                } else if (s.startsWith("consumer")) {
                    var consumerModules = parseConsumerModules(s);
                    vo.consumerConfig = ConsumerProperties.valueOf(consumerModules);
                } else {
                    providerAddress = s;
                }
            }
            return vo;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
    }

    /**
     * str To 服务提供者
     * @param str
     * @return
     */
    private static List<ProviderModule> parseProviderModules(String str) {
        var moduleSplits = StringUtils.substringBeforeLast(
                StringUtils.substringAfterFirst(str, StringUtils.LEFT_SQUARE_BRACKET)
                , StringUtils.RIGHT_SQUARE_BRACKET).split(StringUtils.COMMA);

        var modules = Arrays.stream(moduleSplits)
                .map(it -> it.trim())
                .map(it -> it.split(StringUtils.HYPHEN))
                .map(it -> new ProviderModule(new ProtocolModule(Byte.parseByte(it[0]), it[1]), it[2]))
                .collect(Collectors.toList());
        return modules;
    }

    /**
     * str To 消费者
     * @param str
     * @return
     */
    private static List<ConsumerModule> parseConsumerModules(String str) {
        var moduleSplits = StringUtils.substringBeforeLast(
                StringUtils.substringAfterFirst(str, StringUtils.LEFT_SQUARE_BRACKET)
                , StringUtils.RIGHT_SQUARE_BRACKET).split(StringUtils.COMMA);

        var modules = Arrays.stream(moduleSplits)
                .map(it -> it.trim())
                .map(it -> it.split(StringUtils.HYPHEN))
                .map(it -> new ConsumerModule(new ProtocolModule(Byte.parseByte(it[0]), it[1]), it[2], it[3]))
                .collect(Collectors.toList());
        return modules;
    }

    public String toConsumerString() {
        return this +
                StringUtils.SPACE + StringUtils.VERTICAL_BAR + StringUtils.SPACE +
                uuid;
    }
}
