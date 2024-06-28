package com.awake.orm.cache.persister;

import com.awake.orm.model.IEntity;
import com.awake.util.time.TimeUtils;
import org.apache.commons.lang.SerializationUtils;

/**
 * @version : 1.0
 * @ClassName: Persister Node
 * @Description: 需要持久化的一个节点
 * @Auther: awake
 * @Date: 2023/11/20 17:23
 **/
public class PNode<E extends IEntity<?>> {
    /**
     * 写入数据库的时间
     */
    private volatile long writeToDbTime;
    /**
     * 修改数据的时间
     */
    private volatile long modifiedTime;

    private volatile E entity;

    private E cloneEntity;

    /**
     * 记录最初访问时的线程信息
     */
    private long threadId;

    public PNode(E entity) {
        this.entity = entity;
        this.cloneEntity = (E) SerializationUtils.clone(entity);
        var currentTime = TimeUtils.now();
        this.writeToDbTime = currentTime;
        this.modifiedTime = currentTime;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public E getCloneEntity() {
        return cloneEntity;
    }

    public void setCloneEntity(E cloneEntity) {
        this.cloneEntity = cloneEntity;
    }

    public long getWriteToDbTime() {
        return writeToDbTime;
    }

    public void setWriteToDbTime(long writeToDbTime) {
        this.writeToDbTime = writeToDbTime;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

}
