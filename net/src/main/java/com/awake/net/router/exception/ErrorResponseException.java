package com.awake.net.router.exception;

import com.awake.net.packet.common.Error;

/**
 * @version : 1.0
 * @ClassName: ErrorResponseException
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/7 17:22
 **/
public class ErrorResponseException extends RuntimeException {

    public ErrorResponseException(Error error) {
        super(error.toString());
    }

}
