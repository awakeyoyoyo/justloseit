package com.awake.storage.model;

import com.awake.storage.util.function.Func1;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: IStorage
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:22
 **/
public interface IStorage<K, V> {

    boolean contain(K id);

    boolean contain(int id);

    boolean contain(long id);

    V get(K id);

    V get(int id);

    V get(long id);

    Collection<V> getAll();

    Map<K, V> getData();

    <INDEX> List<V> getIndexes(Func1<V, INDEX> func, INDEX index);

    @Nullable
    <INDEX> V getUniqueIndex(Func1<V, INDEX> func, INDEX uindex);

    int size();

    void recycleStorage();

    boolean isRecycle();

    void setRecycle(boolean recycle);

    IdDef getIdDef();
}
