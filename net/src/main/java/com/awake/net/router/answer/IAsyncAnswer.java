package com.awake.net.router.answer;

import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: IAsyncAnswer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/8 19:53
 **/
public interface IAsyncAnswer <T>  {

    /**
     * 发送完消息后处理
     */
    IAsyncAnswer<T> thenAccept(Consumer<T> consumer);

    /**
     * 接收到异步返回的消息，并处理这个消息，异步请求必须要调用这个方法
     */
    void whenComplete(Consumer<T> consumer);

    /**
     * 如果异步请求没有成功返回，那么就会回调该方法
     */
    IAsyncAnswer<T> notComplete(Runnable notCompleteCallback);
}
