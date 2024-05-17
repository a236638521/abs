package com.m7.abs.support.core.exception;

/**
 * @author zhuhf
 */
public class PushDataFailedException extends NsqTaskException {
    public PushDataFailedException() {
        super();
    }

    public PushDataFailedException(String message) {
        super(message);
    }

    public PushDataFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PushDataFailedException(Throwable cause) {
        super(cause);
    }

    protected PushDataFailedException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
