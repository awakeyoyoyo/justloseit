package com.awakeyo.exception;

import com.awakeyo.util.StringUtils;

/**
 * @version : 1.0
 * @ClassName: UnknownException
 * @Auther: awake
 * @Date: 2023/3/10 15:06
 **/
public class UnknownException extends RuntimeException{

    public UnknownException() {
        super();
    }

    public UnknownException(Throwable cause) {
        super(cause);
    }

    public UnknownException(String message) {
        super(message);
    }

    public UnknownException(String template, Object... args) {
        super(StringUtils.format(template, args));
    }

    public UnknownException(Throwable cause, String message) {
        super(message, cause);
    }

    public UnknownException(Throwable cause, String template, Object... args) {
        super(StringUtils.format(template, args), cause);
    }

}
