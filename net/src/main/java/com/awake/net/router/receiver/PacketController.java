package com.awake.net.router.receiver;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: PacketController
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/19 14:27
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PacketController {

    int moduleId();

}

