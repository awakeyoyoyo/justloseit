package com.awake.util;

import com.awake.exception.AssertException;
import com.awake.util.base.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: AssertionUtils
 * @Description: AssertionUtils class that assists in validating arguments.
 * 用于检测参数是否规范  抛出{@link AssertException}
 * @Auther: awake
 * @Date: 2023/3/9 16:39
 **/
public class AssertionUtils {
    // ----------------------------------bool----------------------------------

    /**
     * Assert a boolean expression, throwing {@code IllegalArgumentException}
     * if the test result is {@code false}.
     *
     * @param expression a boolean expression
     * @param message    the exception message to use if the assertion fails
     * @throws IllegalArgumentException if expression is {@code false}
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new AssertException(message);
        }
    }

    /**
     * 可支持带参数format的类型：类{}的成员变量：{}不能有set方法：{}
     *
     * @param expression 表达式
     * @param format     格式
     * @param args       参数
     */
    public static void isTrue(boolean expression, String format, Object... args) {
        if (!expression) {
            throw new AssertException(format, args);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }


    // ----------------------------------collection----------------------------------

    /**
     * Assert that a collection has elements; that is, it must not be
     * {@code null} and must have at least one element.
     *
     * @param collection the collection to check
     * @param message    the exception message to use if the assertion fails
     * @throws AssertException if the collection is {@code null} or has no elements
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection,
                "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    /**
     * Assert that a Map has entries; that is, it must not be {@code null}
     * and must have at least one entry.
     *
     * @param map     the map to check
     * @param message the exception message to use if the assertion fails
     * @throws AssertException if the map is {@code null} or has no entries
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }


    // ----------------------------------object----------------------------------

    /**
     * Assert that an object is {@code null} .
     *
     * @param object  the object to check
     * @param message the exception message to use if the assertion fails
     * @throws AssertException if the object is not {@code null}
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new AssertException(message);
        }
    }

    public static void isNull(Object object, String format, Object... args) {
        if (object != null) {
            throw new AssertException(format, args);
        }
    }

    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new AssertException(message);
        }
    }

    public static void notNull(Object object, String format, Object... args) {
        if (object == null) {
            throw new AssertException(format, args);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void notNull(Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            notNull(objects[i], "the [index:{}] of objects must not be null", i);
        }
    }

    /**
     * Assert that the provided object is an instance of the provided class.
     *
     * @param type    the type to check against
     * @param obj     the object to check
     * @param message a message which will be prepended to the message produced by
     *                the function itself, and which may be used to provide context. It should
     *                normally end in ":" or "." so that the generated message looks OK when
     *                appended to it.
     * @throws AssertException if the object is not an instance of clazz
     * @see Class#isInstance
     */
    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new AssertException(
                    (StringUtils.isNotBlank(message) ? message + " " : "") +
                            "Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
                            "] must be an instance of " + type);
        }
    }

    public static void isInstanceOf(Class<?> clazz, Object obj) {
        isInstanceOf(clazz, obj, "");
    }

    /**
     * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
     * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
     *
     * @param superType the super type to check against
     * @param subType   the sub type to check
     * @param message   a message which will be prepended to the message produced by
     *                  the function itself, and which may be used to provide context. It should
     *                  normally end in ":" or "." so that the generated message looks OK when
     *                  appended to it.
     * @throws AssertException if the classes are not assignable
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new AssertException((StringUtils.isNotBlank(message) ? message + " " : "")
                    + subType + " is not assignable to " + superType);
        }
    }
}
