package com.awake.storage.model;

import lombok.Data;
import org.springframework.core.io.Resource;

/**
 * @version : 1.0
 * @ClassName: StorageDefinition
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:22
 **/
@Data
public class StorageDefinition {

    private final Class<?> clazz;
    private final Resource resource;

    public StorageDefinition(Class<?> clazz, Resource resource) {
        this.clazz = clazz;
        this.resource = resource;
    }
}
