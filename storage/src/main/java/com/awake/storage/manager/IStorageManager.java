package com.awake.storage.manager;

import com.awake.storage.config.StorageProperties;
import com.awake.storage.model.IStorage;
import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: IstorageManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:41
 **/
public interface IStorageManager {


    /**
     * 配置表初始化之前，先读取所有的excel
     */
    void initBefore();

    /**
     * 注入
     */
    void inject();

    /**
     * 程序加载过后，移除没有用到的配置表
     */
    void initAfter();

    <K, V, T extends IStorage<K, V>> T  getStorage(Class<V> clazz);

    Map<Class<?>, IStorage<?, ?>> storageMap();

    void updateStorage(Class<?> clazz, IStorage<?, ?> storage);

    StorageProperties storageConfig();

    Resource resource(Class<?> clazz);
}
