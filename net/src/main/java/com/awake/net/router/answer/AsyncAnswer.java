package com.awake.net.router.answer;

import com.awake.net.router.attachment.SignalAttachment;
import com.awake.util.base.ThreadUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @version : 1.0
 * @ClassName: AsyncAnswer
 * @Description: 异步响应包
 * @Auther: awake
 * @Date: 2023/8/8 20:16
 **/
@Data
public class AsyncAnswer <T> implements IAsyncAnswer<T> {

    /**
     * 异步回调包
     */
    private T futurePacket;

    /**
     * 异步完成后执行
     */
    private final List<Consumer<T>> consumerList = new ArrayList<>(2);

    /**
     * 携带新号包
     */
    private SignalAttachment signalAttachment;

    /**
     *  成功返回会调用
     */
    private Runnable askCallback;

    /**
     * 没有成功后的回调
     */
    private Runnable notCompleteCallback;

    @Override
    public IAsyncAnswer<T> thenAccept(Consumer<T> consumer) {
        consumerList.add(consumer);
        return this;
    }

    @Override
    public void whenComplete(Consumer<T> consumer) {
        thenAccept(consumer);

        // 这里其实会触发发送消息
        askCallback.run();
    }

    @Override
    public IAsyncAnswer<T> notComplete(Runnable notCompleteCallback) {
        this.notCompleteCallback = ThreadUtils.safeRunnable(notCompleteCallback);;
        return this;
    }

    public void consume() {
        consumerList.forEach(it -> it.accept(futurePacket));
    }

}
