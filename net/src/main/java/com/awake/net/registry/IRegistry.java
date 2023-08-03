package com.awake.net.registry;

import org.apache.zookeeper.CreateMode;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: IRegistry
 * @Description: 注册中心接口
 * @Auther: awake
 * @Date: 2023/7/31 10:28
 **/
public interface IRegistry {

    void start();

    void checkConsumer();

    void addData(String path, byte[] bytes, CreateMode mode);

    void removeData(String path);

    byte[] queryData(String path);

    boolean haveNode(String path);

    List<String> children(String path);

    Set<RegisterVo> remoteProviderRegisterSet();

    /**
     * 监听path路径下的更新
     *
     * @param listenerPath   需要监听的路径
     * @param updateCallback 回调方法，第一个参数是路径，第二个是变化的内容
     * @param removeCallback 回调方法，第一个参数是路径，第二个是变化的内容
     */
    void addListener(String listenerPath, @Nullable BiConsumer<String, byte[]> updateCallback, @Nullable Consumer<String> removeCallback);

    void shutdown();
}
