package com.hello.resource;

import com.awake.storage.anno.Id;
import com.awake.storage.anno.Storage;


/**
 * @Author：lqh
 * @Date：2024/4/2 16:18
 */
@Storage
public class FilterWordResource {

    @Id
    private int id;

    private String filter;

    public int getId() {
        return id;
    }

    public String getFilter() {
        return filter;
    }

}
