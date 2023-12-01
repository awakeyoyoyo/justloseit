package com.awake.storage.manager;

import com.awake.storage.config.StorageProperties;
import com.awake.storage.model.IStorage;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: StorageManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:42
 **/
public class StorageManager implements IStorageManager {
    @Override
    public void initBefore() {

    }

    @Override
    public void inject() {

    }

    @Override
    public void initAfter() {

    }

    @Override
    public <K, V, T extends IStorage<K, V>> T getStorage(Class<V> clazz) {
        return null;
    }

    @Override
    public Map<Class<?>, IStorage<?, ?>> storageMap() {
        return null;
    }

    @Override
    public void updateStorage(Class<?> clazz, IStorage<?, ?> storage) {

    }

    @Override
    public StorageProperties storageConfig() {
        return null;
    }
}
