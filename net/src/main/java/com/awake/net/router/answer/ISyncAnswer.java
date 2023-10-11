package com.awake.net.router.answer;

import com.awake.net.router.attachment.SignalAttachment;

/**
 * @version : 1.0
 * @ClassName: ISyncAnswer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/8 19:53
 **/
public interface ISyncAnswer<T> {
    /**
     * 同步请求的返回数据包
     */
    T packet();

    /**
     * 同步和异步请求的附件
     */
    SignalAttachment attachment();
}
