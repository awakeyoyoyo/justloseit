package com.awake.storage.manager;

import com.awake.storage.model.IdDef;
import com.awake.storage.model.IndexDef;
import com.awake.util.AssertionUtils;
import com.awake.util.ReflectionUtils;
import io.netty.util.collection.LongObjectHashMap;

import java.util.*;

/**
 * @version : 1.0
 * @ClassName: StorageLong
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 15:35
 **/
public class StorageLong<K, V> extends AbstractStorage<K, V> {

    private LongObjectHashMap<V> dataMap;

    public StorageLong(Class<?> clazz, IdDef idDef, Map<String, IndexDef> indexDefMap, List<?> values) {
        super(clazz, idDef, indexDefMap, values);

        this.dataMap = new LongObjectHashMap<>(values.size());
        for (var value : values) {
            var id = (Long) ReflectionUtils.getField(idDef.getField(), value);
            @SuppressWarnings("unchecked")
            var v = (V) value;
            dataMap.put(id, v);
        }
    }


    @Override
    public boolean contain(K id) {
        return contain((long) id);
    }

    @Override
    public boolean contain(int id) {
        return contain((long) id);
    }

    @Override
    public boolean contain(long id) {
        return dataMap.containsKey(id);
    }

    @Override
    public V get(K id) {
        return get((long) id);
    }

    @Override
    public V get(int id) {
        return get((long) id);
    }

    @Override
    public V get(long id) {
        V result = dataMap.get(id);
        AssertionUtils.notNull(result, "The static resource represented as [id:{}] in the static resource [resource:{}] does not exist", id, clazz.getSimpleName());
        return result;
    }

    @Override
    public Collection<V> getAll() {
        return new ArrayList<>(dataMap.values());
    }

    @Override
    public Map<K, V> getData() {
        @SuppressWarnings("unchecked")
        var map = (Map<K, V>) Collections.unmodifiableMap(dataMap);
        return map;
    }

    @Override
    public int size() {
        return dataMap.size();
    }

    @Override
    public void recycleStorage() {
        super.recycleStorage();
        dataMap = null;
    }

}

