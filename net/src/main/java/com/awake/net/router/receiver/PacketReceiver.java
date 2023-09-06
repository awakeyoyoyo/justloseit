package com.awake.net.router.receiver;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: PacketReceiver
 * @Description: 处理协议方法
 * @Auther: awake
 * @Date: 2023/9/6 11:36
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PacketReceiver {
}
