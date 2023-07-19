package com.awake.event;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0
 * @ClassName: EventTestConfiguration
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/19 21:12
 **/
@Configuration
@ComponentScan({"com.awakeyo.event","com.awake.event"})
public class EventTestConfiguration {
}
