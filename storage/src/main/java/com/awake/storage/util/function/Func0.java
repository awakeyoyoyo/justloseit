package com.awake.storage.util.function;

import java.io.Serializable;

/**
 * @version : 1.0
 * @ClassName: Func0
 * @Description:
 * @Auther: awake
 * @Date: 2023/12/1 11:25
 **/
@FunctionalInterface
public interface Func0<R> extends Serializable {
    /**
     * 执行函数
     *
     * @return 函数执行结果
     * @throws Exception 自定义异常
     */
    R apply() throws Exception;

}
