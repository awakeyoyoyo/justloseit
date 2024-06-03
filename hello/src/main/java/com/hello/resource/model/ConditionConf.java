package com.hello.resource.model;

import lombok.Getter;

/**
 * @Author：lqh
 * @Date：2024/6/3 20:10
 */
@Getter
public class ConditionConf {

    /**
     * 条件类型
     */
    private int conditionType;

    /**
     * 条件参数
     */
    private String params;

}
