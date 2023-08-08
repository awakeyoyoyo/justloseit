package com.awake.util;

import com.awake.exception.RunException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @version : 1.0
 * @ClassName: JsonUtils
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/8 21:07
 **/
public class JsonUtils {
    /**
     * 适用于任何场景下的json转换，只要在各个类方法中不调用configure方法，则MAPPER都是线程安全的
     */
    public static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * 涡轮增压的jackson，会使用字节码增强技术
     */
    public static final ObjectMapper MAPPER_TURBO = new ObjectMapper();

    static {
        //序列化
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //序列化枚举是以toString()来输出，默认false，即默认以name()来输出
        // MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);


        //反序列化
        //当反序列化有未知属性则抛异常，true打开这个设置
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        //美化输出
        DefaultPrettyPrinter prettyPrinter = (DefaultPrettyPrinter) MAPPER.getSerializationConfig().getDefaultPrettyPrinter();
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(StringUtils.TAB_ASCII, FileUtils.LS);
        prettyPrinter.indentObjectsWith(indenter);

        MAPPER_TURBO.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER_TURBO.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER_TURBO.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        MAPPER_TURBO.registerModule(new AfterburnerModule());
    }

    public static <T> T string2Object(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RunException("将json字符串[json:{}]转换为对象[class:{}]时异常", json, clazz, e);
        }
    }

    //普通输出
    public static String object2String(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RunException("将对象[object:{}]转换为json字符串时异常", object, e);
        }
    }

    // 格式化/美化/优雅的输出
    public static String object2StringPrettyPrinter(Object object) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new RunException("将对象[object:{}]转换为json字符串时异常", object, e);
        }
    }

    /**
     * 涡轮增压极大提升json转换性能，字节码增强
     */
    public static <T> T string2ObjectTurbo(String json, Class<T> clazz) {
        try {
            return MAPPER_TURBO.readValue(json, clazz);
        } catch (Exception e) {
            throw new RunException("将json字符串[json:{}]转换为对象[class:{}]时异常", json, clazz, e);
        }
    }

    /**
     * 涡轮增压极大提升json转换性能，字节码增强
     */
    public static String object2StringTurbo(Object object) {
        try {
            return MAPPER_TURBO.writeValueAsString(object);
        } catch (Exception e) {
            throw new RunException("将对象[object:{}]转换为json字符串时异常", object, e);
        }
    }

    public static <T> List<T> string2List(String json, Class<T> clazz) {
        CollectionType collectionType = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        try {
            return MAPPER.readValue(json, collectionType);
        } catch (Exception e) {
            throw new RunException("将json字符串[json:{}]转换为List[{}]异常", json, clazz, e);
        }
    }

    //元素不可重复
    public static <T> Set<T> string2Set(String json, Class<T> clazz) {
        CollectionType collectionType = MAPPER.getTypeFactory().constructCollectionType(HashSet.class, clazz);
        try {
            return MAPPER.readValue(json, collectionType);
        } catch (Exception e) {
            throw new RunException("将json字符串[json:{}]转换为Set[{}]异常", json, clazz, e);
        }
    }

    public static <C extends Collection<T>, T> C string2Collection(String json, Class<C> collectionType, Class<T> elementType) {
        try {
            CollectionType ct = MAPPER.getTypeFactory().constructCollectionType(collectionType, elementType);
            return MAPPER.readValue(json, ct);
        } catch (IOException e) {
            throw new RunException("将json字符串[json:{}]转换为Collection[{}]异常", json, collectionType, e);
        }
    }

    public static <K, V> Map<K, V> string2Map(String json, Class<K> kClazz, Class<V> vClazz) {
        MapType mapType = MAPPER.getTypeFactory().constructMapType(HashMap.class, kClazz, vClazz);
        try {
            return MAPPER.readValue(json, mapType);
        } catch (Exception e) {
            throw new RunException("将json字符串[json:{}]转换为Map[[key:{}], [value:{}]]异常", json, kClazz, vClazz, e);
        }
    }

    public static <T> T[] string2Array(String json, Class<T> clazz) {
        ArrayType arrayType = MAPPER.getTypeFactory().constructArrayType(clazz);
        try {
            return MAPPER.readValue(json, arrayType);
        } catch (Exception e) {
            throw new RunException("将json字符串[{}]转换为数组[array:{}]异常", json, clazz, e);
        }
    }

    public static Map<String, String> getJsonMap(String json) {
        try {
            HashMap jsonMap = new HashMap<String, String>();
            ArrayDeque queue = new ArrayDeque<JsonNode>();
            // 将Json串以树状结构读入内存
            JsonNode rootNode = MAPPER.readTree(json);
            queue.add(rootNode);
            // 深度优先遍历算法
            while (!queue.isEmpty()) {
                JsonNode pollNode = (JsonNode) queue.poll();
                Iterator<Entry<String, JsonNode>> iterator = pollNode.fields();
                // 循环遍历子节点下的信息
                while (iterator.hasNext()) {
                    Entry node = iterator.next();
                    String field = (String) node.getKey();
                    String value = node.getValue().toString();
                    jsonMap.put(field, value);
                }
            }
            return jsonMap;
        } catch (IOException e) {
            throw new RunException("将json字符串[json:{}]转换为jsonMap异常", json, e);
        }
    }
}
