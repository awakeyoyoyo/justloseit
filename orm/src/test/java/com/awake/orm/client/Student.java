package com.awake.orm.client;

/**
 * @version : 1.0
 * @ClassName: Student
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/30 11:19
 **/
public class Student {

    private long id;
    private String name;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
