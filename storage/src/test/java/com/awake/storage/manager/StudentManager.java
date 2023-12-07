package com.awake.storage.manager;

/**
 * @version : 1.0
 * @ClassName: StudentManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:48
 **/

import com.awake.storage.anno.StorageAutowired;
import com.awake.storage.model.IStorage;
import com.awake.storage.resource.StudentCsvResource;
import com.awake.storage.resource.StudentResource;
import org.springframework.stereotype.Component;

@Component
public class StudentManager {

    @StorageAutowired
    public IStorage<Integer, StudentResource> studentResources;
    @StorageAutowired
    public IStorage<Integer, StudentCsvResource> studentCsvResources;

}
