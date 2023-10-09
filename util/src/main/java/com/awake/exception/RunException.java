package com.awake.exception;

import com.awake.util.base.StringUtils;

/**
 * @version : 1.0
 * @ClassName: RunException
 * @Description: 异常
 * @Auther: awake
 * @Date: 2023/3/10 15:05
 **/
public class RunException extends RuntimeException {

    public RunException(Throwable cause) {
        super(cause);
    }

    public RunException(String message) {
        super(message);
    }

    public RunException(String message, Throwable cause) {
        super(message, cause);
    }

    public RunException(String template, Object arg1, Throwable cause) {
        super(StringUtils.format(template, arg1), cause);
    }

    public RunException(String template, Object arg1, Object arg2, Throwable cause) {
        super(StringUtils.format(template, arg1, arg2), cause);
    }

    public RunException(String template, Object arg1, Object arg2, Object arg3, Throwable cause) {
        super(StringUtils.format(template, arg1, arg2, arg3), cause);
    }

    public RunException(String template, Object arg1, Object arg2, Object arg3, Object arg4, Throwable cause) {
        super(StringUtils.format(template, arg1, arg2, arg3, arg4), cause);
    }

    public RunException(String template, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Throwable cause) {
        super(StringUtils.format(template, arg1, arg2, arg3, arg4, arg5), cause);
    }

    public RunException(String template, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Throwable cause) {
        super(StringUtils.format(template, arg1, arg2, arg3, arg4, arg5, arg6), cause);
    }

    public RunException(String template, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Throwable cause) {
        super(StringUtils.format(template, arg1, arg2, arg3, arg4, arg5, arg6, arg7), cause);
    }

    public RunException(String template, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Throwable cause) {
        super(StringUtils.format(template, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8), cause);
    }

    public RunException(String template, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Throwable cause) {
        super(StringUtils.format(template, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9), cause);
    }

    public RunException(String template, Object... args) {
        super(StringUtils.format(template, args));
    }
}
