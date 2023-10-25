package com.awake.net.collection;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version : 1.0
 * @ClassName: ConcurrentHashSet
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/10/19 11:34
 **/
public class ConcurrentHashSet<E> extends AbstractSet<E> {

    private final Map<E, Boolean> map= new ConcurrentHashMap<>();

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, Boolean.TRUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

}
