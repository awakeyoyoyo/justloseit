package com.awake.net.router.exception;

import com.awake.exception.RunException;

/**
 * @version : 1.0
 * @ClassName: NetTimeOutException
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 17:22
 **/
public class NetTimeOutException extends RunException {

    public NetTimeOutException(String template, Object... args) {
        super(template, args);
    }

}