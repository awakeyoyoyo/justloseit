package com.awake.storage.util.function;

import java.io.Serializable;

/**
 * @version : 1.0
 * @ClassName: Func1
 * @Description:
 * @Auther: awake
 * @Date: 2023/12/1 11:25
 **/
@FunctionalInterface
public interface Func1<P, R> extends Serializable {

    /**
     * 执行函数
     *
     * @param parameter 参数
     * @return 函数执行结果
     * @throws Exception 自定义异常
     */
    R apply(P parameter) throws Exception;

}