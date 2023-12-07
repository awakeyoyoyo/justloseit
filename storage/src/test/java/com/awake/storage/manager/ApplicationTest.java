package com.awake.storage.manager;

import com.awake.storage.StorageContext;
import com.awake.storage.model.IStorage;
import com.awake.storage.resource.StudentCsvResource;
import com.awake.storage.resource.StudentResource;
import com.awake.storage.resource.TeacherResource;
import com.awake.storage.resource.TestResource;
import com.awake.util.AssertionUtils;
import com.awake.util.JsonUtils;
import com.awake.util.base.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: ApplicationTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:49
 **/
@SpringBootTest(classes = {StorageAutoTestConfiguration.class})
public class ApplicationTest {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

    @Autowired
    private StorageManager storageManager;
    @Autowired
    private StudentManager studentManager;
    @Autowired
    private TestManager testManager;
    // storage教程
    @Test
    public void startStorageTest() {
        // 加载配置文件，配置文件中必须引入storage
        // 配置文件中scan，需要映射Excel的类所在位置，会自动搜索文件夹下的Excel文件，Excel文件可以放在指定文件夹的任意目录
        // 配置文件中resource，需要映射Excel的文件所在位置

        // Excel的映射内容需要在被Spring管理的bean的方法上加上@ResInjection注解，即可自动注入Excel对应的对象
        // 参考StudentManager中的标准用法

        IStorage<Integer, StudentResource> storage1 = StorageContext.getStorageManager().getStorage(StudentResource.class);

        //根据主键获取数据
        StudentResource studentResource = StorageContext.get(StudentResource.class, 1001);
        TeacherResource teacherResource = StorageContext.get(TeacherResource.class, 1001);
        //获取名字索引列表
        List<StudentResource> nameIndexList = StorageContext.getIndexes(StudentResource.class, StudentResource::getName, "james1");
        //获取年龄索引列表
        List<StudentResource> ageIndexList = StorageContext.getIndexes(StudentResource.class, StudentResource::getAge, 10);

        //获取Storage
        IStorage<Integer, StudentResource> storage = storageManager.getStorage(StudentResource.class);

        var studentResources = studentManager.studentResources;
        var studentCsvResources = studentManager.studentCsvResources;
        // @Resource注解没指定别名，类名称和Excel名称必须完全一致，没有使用@ExcelFieldName对象属性名会自动对应同名的资源文件列名
        for (StudentResource resource : studentResources.getAll()) {
            logger.info(JsonUtils.object2String(resource));
        }
        System.out.println(StringUtils.MULTIPLE_HYPHENS);

        // 通过id找到对应的行
        var id = 1002;
        var valueById = studentResources.get(id);
        logger.info(JsonUtils.object2String(valueById));
        System.out.println(StringUtils.MULTIPLE_HYPHENS);

        // 通过索引找对应的行
        var valuesByIndex = studentResources.getIndexes(StudentResource::getName, "james0");
        logger.info(JsonUtils.object2String(valuesByIndex));

        // 通过索引找对应的行
        var csvValuesByIndex = studentCsvResources.getIndexes(StudentCsvResource::getName, "james0");
        logger.info(JsonUtils.object2String(csvValuesByIndex));

        // Excel的映射内容需要在被Spring管理的bean的方法上加上@ResInjection注解，即可自动注入Excel对应的对象
        var testResources = testManager.testResources;
        for (TestResource resource : testResources.getAll()) {
            Map<Integer, String> map = resource.getType9();
            AssertionUtils.notNull(map.get(1));
            logger.info(JsonUtils.object2String(resource));
        }
        // 通过id找到对应的行
        id = 2;
        var resource = testResources.get(id);
        logger.info(JsonUtils.object2String(resource));
        System.out.println(StringUtils.MULTIPLE_HYPHENS);
    }
}
