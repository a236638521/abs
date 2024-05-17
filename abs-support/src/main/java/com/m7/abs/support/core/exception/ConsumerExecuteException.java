package com.m7.abs.support.core.exception;

/**
 * @author zhuhf
 */
public class ConsumerExecuteException extends RuntimeException {
    public ConsumerExecuteException() {
        super();
    }

    public ConsumerExecuteException(String message) {
        super(message);
    }

    public ConsumerExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsumerExecuteException(Throwable cause) {
        super(cause);
    }

    protected ConsumerExecuteException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
