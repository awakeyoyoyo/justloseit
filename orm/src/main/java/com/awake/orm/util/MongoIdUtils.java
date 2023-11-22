package com.awake.orm.util;

import com.awake.orm.OrmContext;
import com.awake.util.AssertionUtils;
import com.awake.util.base.CollectionUtils;
import com.awake.util.base.StringUtils;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: MongoIdUtils
 * @Description: mongoUtils
 * @Auther: awake
 * @Date: 2023/11/22 18:01
 **/
public class MongoIdUtils {

    private static final long INIT_ID = 1L;

    private static final String COLLECTION_NAME = "uuid";

    private static final String COUNT = "count";


    /**
     * 分布式唯一Id生成器，利用MongoDB数据库存储自增的ID，可以保证原子性，一致性。
     * <p>
     * 线程安全，不同线程互不影响。
     * <p>
     * 进程安全，不同的应用进程也可以保证唯一id。
     *
     * @param collectionName 存储的集合名称
     * @param documentName   文档id
     * @return 增加后的唯一id
     */
    public static long getIncrementIdFromMongo(String collectionName, String documentName) {
        var collection = OrmContext.getOrmManager().getCollection(collectionName);

        var document = collection.findOneAndUpdate(Filters.eq("_id", documentName)
                , new Document("$inc", new Document(COUNT, 1L)));

        if (document == null) {
            var result = collection.insertOne(new Document("_id", documentName).append(COUNT, INIT_ID));
            AssertionUtils.notNull(result.getInsertedId());
            return INIT_ID;
        }

        return document.getLong("count") + 1;
    }

    public static long getIncrementIdFromMongoDefault(String documentName) {
        return getIncrementIdFromMongo(COLLECTION_NAME, documentName);
    }

    public static long getIncrementIdFromMongoDefault(Class<?> clazz) {
        return getIncrementIdFromMongo(COLLECTION_NAME, StringUtils.uncapitalize(clazz.getSimpleName()));
    }


    // ----------------------------------------------------------------------------------------------------

    /**
     * 重置documentName的数值，重置为0
     *
     * @param documentName 档id
     */
    public static void resetIncrementIdFromMongoDefault(String documentName) {
        setIncrementIdFromMongo(COLLECTION_NAME, documentName, 0L);
    }

    public static void setIncrementIdFromMongo(Class<?> clazz, long value) {
        setIncrementIdFromMongo(COLLECTION_NAME, StringUtils.uncapitalize(clazz.getSimpleName()), value);
    }

    public static void setIncrementIdFromMongo(String documentName, long value) {
        setIncrementIdFromMongo(COLLECTION_NAME, documentName, value);
    }

    public static void setIncrementIdFromMongo(String collectionName, String documentName, long value) {
        var collection = OrmContext.getOrmManager().getCollection(collectionName);
        var document = collection.findOneAndUpdate(Filters.eq("_id", documentName), new Document("$set", new Document(COUNT, value)));

        if (document == null) {
            var result = collection.insertOne(new Document("_id", documentName).append(COUNT, value));
            AssertionUtils.notNull(result.getInsertedId());
        }
    }


    // ----------------------------------------------------------------------------------------------------

    /**
     * 获取最大的id
     *
     * @param collectionName 存储的集合名称
     * @param documentName   文档id
     * @return 增加后的唯一id
     */
    public static long getMaxIdFromMongo(String collectionName, String documentName) {
        var collection = OrmContext.getOrmManager().getCollection(collectionName);

        var list = new ArrayList<Document>();
        collection.find(Filters.eq("_id", documentName)).forEach(new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                list.add(document);
            }
        });

        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        return list.get(0).getLong("count");
    }

    public static long getMaxIdFromMongoDefault(String documentName) {
        return getMaxIdFromMongo(COLLECTION_NAME, documentName);
    }

    public static long getMaxIdFromMongoDefault(Class<?> clazz) {
        return getMaxIdFromMongo(COLLECTION_NAME, StringUtils.uncapitalize(clazz.getSimpleName()));
    }

}
