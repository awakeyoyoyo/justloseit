package com.awake.storage.resource;

import com.awake.storage.anno.Id;
import com.awake.storage.anno.Index;
import com.awake.storage.anno.Storage;
import com.awake.storage.resource.model.User;
import lombok.Getter;

/**
 * @version : 1.0
 * @ClassName: StudentCsvResource
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:30
 **/
@Storage("StudentCsvResource")
@Getter
public class StudentCsvResource {

    @Id
    private int id;

    /**
     * 索引，默认为可重复的索引
     */
    @Index
    private String name;

    private int age;
    private float score;
    private String[] courses;
    private User[] users;
    private User user;

    /**
     * 不想映射的字段必须加上transient关键字，这样就不会从Excel中去找对应的列
     */
    private transient String notMapContent;

}
