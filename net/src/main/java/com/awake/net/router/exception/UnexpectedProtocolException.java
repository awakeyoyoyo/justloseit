package com.awake.net.router.exception;

import com.awake.exception.RunException;

/**
 * @version : 1.0
 * @ClassName: UnexpectedProtocolException
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 17:23
 **/
public class UnexpectedProtocolException extends RunException {

    public UnexpectedProtocolException(String template, Object... args) {
        super(template, args);
    }

}

