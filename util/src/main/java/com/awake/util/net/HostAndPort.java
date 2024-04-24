package com.awake.util.net;

import com.awake.util.base.CollectionUtils;
import com.awake.util.base.StringUtils;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @version : 1.0
 * @ClassName: HostAndPort
 * @Description: 地址和端口
 * @Auther: awake
 * @Date: 2023/8/1 10:40
 **/
@Data
public class HostAndPort {

    private String host;
    private int port;

    public static HostAndPort valueOf(String host, int port) {
        HostAndPort hostAndPort = new HostAndPort();
        hostAndPort.host = host;
        hostAndPort.port = port;
        return hostAndPort;
    }

    /**
     * @param hostAndPort example -> localhost:port
     */
    public static HostAndPort valueOf(String hostAndPort) {
        String[] split = hostAndPort.trim().split(StringUtils.COLON_REGEX);
        return valueOf(split[0].trim(), Integer.parseInt(split[1].trim()));
    }

    public static List<HostAndPort> toHostAndPortList(String hostAndPorts) {
        if (StringUtils.isEmpty(hostAndPorts)) {
            return Collections.emptyList();
        }

        String[] hostAndPortSplits = hostAndPorts.split(StringUtils.COMMA_REGEX);
        List<HostAndPort> hostAndPortList = new ArrayList<>();
        for (String hostAndPort : hostAndPortSplits) {
            hostAndPortList.add(valueOf(hostAndPort));
        }
        return hostAndPortList;
    }

    public static List<HostAndPort> toHostAndPortList(Collection<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<HostAndPort> hostAndPortList = new ArrayList<>();
        list.forEach(it -> hostAndPortList.addAll(toHostAndPortList(it)));
        return hostAndPortList;
    }

    public static String toHostAndPortListStr(Collection<HostAndPort> list) {
        List<String> urlList = list.stream()
                .map(it -> it.toHostAndPortStr())
                .collect(Collectors.toList());
        return StringUtils.joinWith(StringUtils.COMMA, urlList.toArray());
    }

    public String toHostAndPortStr() {
        return StringUtils.format("{}:{}", this.host.trim(), this.port);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HostAndPort that = (HostAndPort) o;
        return port == that.port && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public String toString() {
        return toHostAndPortStr();
    }
}
