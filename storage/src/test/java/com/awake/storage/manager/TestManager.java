package com.awake.storage.manager;

import com.awake.storage.anno.StorageAutowired;
import com.awake.storage.model.IStorage;
import com.awake.storage.resource.TestResource;
import org.springframework.stereotype.Component;

/**
 * @version : 1.0
 * @ClassName: TestManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:48
 **/
@Component
public class TestManager {

    @StorageAutowired
    public IStorage<Integer, TestResource> testResources;

}
