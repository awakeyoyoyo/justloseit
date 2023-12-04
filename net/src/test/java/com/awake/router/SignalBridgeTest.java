package com.awake.router;

import com.awake.event.manger.EventBus;
import com.awake.net.router.SignalBridge;
import com.awake.net.router.TaskBus;
import com.awake.net.router.attachment.SignalAttachment;
import com.awake.util.base.ThreadUtils;
import com.awake.util.time.TimeUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @version : 1.0
 * @ClassName: SignalBridgeTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:28
 **/
public class SignalBridgeTest {

    private final int executorSize = EventBus.EXECUTORS_SIZE;
    private final int count = 100_0000;
    private final int totalIndex = 10;

    @Test
    public void test() throws InterruptedException {
        System.out.println(executorSize);
        for (int i = 0; i < 10; i++) {
            arrayTest();
        }
        SignalBridge.status();
        System.out.println(SignalAttachment.ATOMIC_ID.get());
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    public void arrayTest() throws InterruptedException {
        var startTime = TimeUtils.currentTimeMillis();

        var countDownLatch = new CountDownLatch(executorSize);
        //每条线程都在跑
        for (var i = 0; i < executorSize; i++) {
            TaskBus.execute(i, new Runnable() {
                @Override
                public void run() {
                    addAndRemoveArray();
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println("costTime:" + (TimeUtils.currentTimeMillis() - startTime));
    }


    public void addAndRemoveArray() {
        var signalList = new ArrayList<Integer>(totalIndex);
        for (var i = 0; i < count; i++) {
            signalList.clear();
            for (var j = 0; j < totalIndex; j++) {
                var signalAttachment = new SignalAttachment();
                SignalBridge.addSignalAttachment(signalAttachment);
                signalList.add(signalAttachment.getSignalId());
            }

            for (var signalId : signalList) {
                SignalBridge.removeSignalAttachment(signalId);
            }
        }
    }


}
