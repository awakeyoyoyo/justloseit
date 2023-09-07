package com.awake.net.protocol.anno;

import java.lang.annotation.*;

/**
 * @version : 1.0
 * @ClassName: Packet
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/8 11:41
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Packet {
    int protocolId() default 0;
}
