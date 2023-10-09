package com.awake.exception;

import com.awake.util.base.StringUtils;

/**
 * @version : 1.0
 * @ClassName: AssertException
 * @Description: 自定义断言异常 --方便接受多个object
 * @Auther: awake
 * @Date: 2023/3/9 16:50
 **/
public class AssertException extends RuntimeException{

    public AssertException(String message) {
        super(message);
    }

    public AssertException(String template, Object... args) {
        super(StringUtils.format(template, args));
    }
}
