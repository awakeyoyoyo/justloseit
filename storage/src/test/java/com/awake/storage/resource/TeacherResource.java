package com.awake.storage.resource;

import com.awake.storage.anno.AliasFieldName;
import com.awake.storage.anno.Id;
import com.awake.storage.anno.Index;
import com.awake.storage.anno.Storage;
import com.awake.storage.resource.model.User;
import lombok.Getter;

import java.util.List;

/**
 * @version : 1.0
 * @ClassName: TeacherResource
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:33
 **/
@Storage
@Getter
public class TeacherResource {
    @Id
    private int id;
    @Index(unique = true)
    private String idCard;
    @Index
    private String name;
    @AliasFieldName("年龄")
    private int age;
    private float score;
    private String[] courses;
    private User[] users;
    private List<User> userList;
    private User user;


}
