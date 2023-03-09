package com.awakeyo.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version : 1.0
 * @ClassName: ArrayUtils
 * @Description: 数组工具类
 * @Auther: awake
 * @Date: 2023/3/8 12:01
 **/
public class ArrayUtils {

    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    /**
     * unbox
     */
    public static boolean booleanValue(Boolean value) {
        return value != null && value;
    }

    public static byte byteValue(Number value) {
        return value == null ? 0 : value.byteValue();
    }

    public static short shortValue(Number value) {
        return value == null ? 0 : value.shortValue();
    }

    public static int intValue(Number value) {
        return value == null ? 0 : value.intValue();
    }

    public static long longValue(Number value) {
        return value == null ? 0 : value.longValue();
    }

    public static float floatValue(Number value) {
        return value == null ? 0 : value.floatValue();
    }

    public static double doubleValue(Number value) {
        return value == null ? 0 : value.doubleValue();
    }

    public static char charValue(Character value) {
        return value == null ? Character.MIN_VALUE : value;
    }

    /**
     * length
     */
    public static int length(boolean[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(byte[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(short[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(int[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(long[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(float[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(double[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(char[] array) {
        return array == null ? 0 : array.length;
    }

    public static <T> int length(T[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * isEmpty
     */
    public static boolean isEmpty(boolean[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(byte[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(short[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(int[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(long[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(float[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(double[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(char[] array) {
        return (array == null || array.length == 0);
    }

    public static <T> boolean isEmpty(T[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * isNotEmpty
     */
    public static boolean isNotEmpty(boolean[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(byte[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(short[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(int[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(long[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(float[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(double[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(char[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }


    /**
     * toList
     */
    public static List<Boolean> toList(boolean[] array) {
        List<Boolean> list = new ArrayList<Boolean>();
        for (Boolean value : array) {
            list.add(value);
        }
        return list;
    }

    public static List<Byte> toList(byte[] array) {
        List<Byte> list = new ArrayList<Byte>();
        for (Byte value : array) {
            list.add(value);
        }
        return list;
    }

    public static List<Short> toList(short[] array) {
        List<Short> list = new ArrayList<Short>();
        for (Short value : array) {
            list.add(value);
        }
        return list;
    }

    public static List<Integer> toList(int[] array) {
        List<Integer> list = new ArrayList<Integer>();
        for (Integer j : array) {
            list.add(j);
        }
        return list;
    }

    public static List<Long> toList(long[] array) {
        List<Long> list = new ArrayList<Long>();
        for (Long j : array) {
            list.add(j);
        }
        return list;
    }

    public static List<Float> toList(float[] array) {
        List<Float> list = new ArrayList<Float>();
        for (Float j : array) {
            list.add(j);
        }
        return list;
    }

    public static List<Double> toList(double[] array) {
        List<Double> list = new ArrayList<Double>();
        for (Double j : array) {
            list.add(j);
        }
        return list;
    }

    public static List<Character> toList(char[] array) {
        List<Character> list = new ArrayList<Character>();
        for (Character j : array) {
            list.add(j);
        }
        return list;
    }

    public static <T> List<T> toList(T[] array) {
        if (isEmpty(array)) {
            return CollectionUtils.emptyList();
        }
        return new ArrayList<>(Arrays.asList(array));
    }


    /**
     * toArray
     */
    public static boolean[] booleanToArray(List<Boolean> list) {
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        int size = list.size();
        boolean[] array = new boolean[size];
        for (int i = 0; i < size; i++) {
            array[i] = booleanValue(list.get(i));
        }
        return array;
    }

    public static byte[] byteToArray(List<Byte> list) {
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_BYTE_ARRAY;
        }
        int size = list.size();
        byte[] array = new byte[size];
        for (int i = 0; i < size; i++) {
            array[i] = byteValue(list.get(i));
        }
        return array;
    }

    public static short[] shortToArray(List<Short> list) {
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_SHORT_ARRAY;
        }
        int size = list.size();
        short[] array = new short[size];
        for (int i = 0; i < size; i++) {
            array[i] = shortValue(list.get(i));
        }
        return array;
    }

    public static int[] intToArray(List<Integer> list) {
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_INT_ARRAY;
        }
        int size = list.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = intValue(list.get(i));
        }
        return array;
    }


    public static long[] longToArray(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_LONG_ARRAY;
        }
        int size = list.size();
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = longValue(list.get(i));
        }
        return array;
    }

    public static float[] floatToArray(List<Float> list) {
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_FLOAT_ARRAY;
        }
        int size = list.size();
        float[] array = new float[size];
        for (int i = 0; i < size; i++) {
            array[i] = floatValue(list.get(i));
        }
        return array;
    }

    public static double[] doubleToArray(List<Double> list) {
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_DOUBLE_ARRAY;
        }
        int size = list.size();
        double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = doubleValue(list.get(i));
        }
        return array;
    }

    public static char[] charToArray(List<Character> list) {
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_CHAR_ARRAY;
        }
        int size = list.size();
        char[] array = new char[size];
        for (int i = 0; i < size; i++) {
            array[i] = charValue(list.get(i));
        }
        return array;
    }

    public static <T> T[] listToArray(List<T> list, Class<T> clazz) {
        AssertionUtils.notNull(list);
        AssertionUtils.notNull(clazz);

        int length = list.size();
        Object objectArray = Array.newInstance(clazz, length);
        return (T[]) copy(list.toArray(), objectArray, length);
    }


    /**
     * copy
     */
    public static boolean[] copy(boolean[] source) {
        if (isEmpty(source)) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        int length = source.length;
        boolean[] target = new boolean[length];
        return (boolean[]) copy(source, target, length);
    }

    public static byte[] copy(byte[] source) {
        if (isEmpty(source)) {
            return EMPTY_BYTE_ARRAY;
        }
        int length = source.length;
        byte[] target = new byte[length];
        return (byte[]) copy(source, target, length);
    }

    public static short[] copy(short[] source) {
        if (isEmpty(source)) {
            return EMPTY_SHORT_ARRAY;
        }
        int length = source.length;
        short[] target = new short[length];
        return (short[]) copy(source, target, length);
    }

    public static int[] copy(int[] source) {
        if (isEmpty(source)) {
            return EMPTY_INT_ARRAY;
        }
        int length = source.length;
        int[] target = new int[length];
        return (int[]) copy(source, target, length);
    }

    public static long[] copy(long[] source) {
        if (isEmpty(source)) {
            return EMPTY_LONG_ARRAY;
        }
        int length = source.length;
        long[] target = new long[length];
        return (long[]) copy(source, target, length);
    }

    public static float[] copy(float[] source) {
        if (isEmpty(source)) {
            return EMPTY_FLOAT_ARRAY;
        }
        int length = source.length;
        float[] target = new float[length];
        return (float[]) copy(source, target, length);
    }

    public static double[] copy(double[] source) {
        if (isEmpty(source)) {
            return EMPTY_DOUBLE_ARRAY;
        }
        int length = source.length;
        double[] target = new double[length];
        return (double[]) copy(source, target, length);
    }

    public static char[] copy(char[] source) {
        if (isEmpty(source)) {
            return EMPTY_CHAR_ARRAY;
        }
        int length = source.length;
        char[] target = new char[length];
        return (char[]) copy(source, target, length);
    }

    public static <T> T[] copy(T[] source, Class<T> clazz) {
        AssertionUtils.notNull(source);
        AssertionUtils.notNull(clazz);
        int length = source.length;
        Object target = Array.newInstance(clazz, length);
        return (T[]) copy(source, target, length);
    }

    private static Object copy(Object source, Object target, int length) {
        System.arraycopy(source, 0, target, 0, length);
        return target;
    }
}
