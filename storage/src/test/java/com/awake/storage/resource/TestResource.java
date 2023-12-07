package com.awake.storage.resource;

import com.awake.storage.anno.Id;
import com.awake.storage.anno.Storage;
import com.awake.storage.resource.model.Item;
import lombok.Getter;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: TestResource
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:34
 **/
@Storage
@Getter
public class TestResource {

    @Id
    private int Id;
    private long Type0;
    private String Type1;
    private String[] Type2;
    private Integer[] Type3;
    private Long[] Type4;
    private Item[] Type5;
    private Map<Integer, Integer> Type8;
    private Map<Integer, String> Type9;
    private Map<String, String> Type10;


}

