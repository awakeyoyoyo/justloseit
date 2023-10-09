package com.awake.util.base;

import com.awake.util.IOUtils;
import com.awake.util.base.StringUtils;

import java.util.*;

/**
 * @version : 1.0
 * @ClassName: CollectionUtils
 * @Description: 集合工具类
 * @Auther: awake
 * @Date: 2023/3/8 14:32
 **/
public class CollectionUtils {

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Return {@code true} if the supplied Map is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }


    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    public static int size(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    public static <T> Iterator<T> iterator(Collection<T> collection) {
        return isEmpty(collection) ? null : collection.iterator();
    }

    public static <K, V> Iterator<Map.Entry<K, V>> iterator(Map<K, V> map) {
        return isEmpty(map) ? null : map.entrySet().iterator();
    }

    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    public static <T> Set<T> emptySet() {
        return new HashSet<>();
    }

    public static <K, V> Map<K, V> emptyMap() {
        return new HashMap<>();
    }

    public static <T> List<T> newList(int size) {
        return size <= 0 ? new ArrayList<T>() : new ArrayList<T>(comfortableLength(size));
    }

    public static <T> Set<T> newSet(int size) {
        return size <= 0 ? new HashSet<T>() : new HashSet<T>(comfortableCapacity(size));
    }

    public static <K, V> Map<K, V> newMap(int size) {
        return size <= 0 ? new HashMap<K,V>() : new HashMap<K,V>(comfortableCapacity(size));
    }

    /**
     * 数组初始化长度的安全上限限制，防止反序列化异常导致内存突然升高
     */
    public static int comfortableLength(int length) {
        if (length >= IOUtils.BYTES_PER_MB) {
            throw new ArrayStoreException(StringUtils.format("新建数组的长度[{}]超过设置的安全范围[{}]", length, IOUtils.BYTES_PER_MB));
        }
        return length;
    }

    /**
     * 计算List和HashMap初始化合适的大小，为了安全必须给初始化的集合一个最大上限，防止反序列化一个不合法的包导致内存突然升高
     */
    public static int comfortableCapacity(int capacity) {
        return capacity < 16
                ? (capacity < 8 ? 16 : 32)
                : (capacity < 32 ? 64 : Math.min(capacity << 1, IOUtils.BYTES_PER_MB));
    }


    /**
     * 获取集合的最后几个元素
     */
    public static <T> List<T> subListLast(List<T> list, int num) {
        if (isEmpty(list)) {
            return emptyList();
        }

        int
                startIndex = list.size() - num;
        if (startIndex <= 0) {
            return new ArrayList<>(list);
        }

        List<T> result = new ArrayList<T>();


        for (T element : list) {
            startIndex--;
            if (startIndex < 0) {
                result.add(element);
            }
        }

        return result;
    }
}
