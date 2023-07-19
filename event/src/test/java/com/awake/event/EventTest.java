package com.awake.event;

import com.awakeyo.event.manger.EventBus;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version : 1.0
 * @ClassName: ApplicationTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/3/3 16:49
 **/
public class EventTest {

    @Test
    public void startEventTest() {
        // load the configuration file which must import event.(加载配置文件，配置文件中必须引入event)
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EventTestConfiguration.class);

        // see receiver method of MyController1 and MyController2
        EventBus.syncSubmit(LoginEvent.valueOf(10001,System.currentTimeMillis()));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
