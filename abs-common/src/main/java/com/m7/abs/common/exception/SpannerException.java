package com.m7.abs.common.exception;

/**
 * 工具类抛出异常
 */
public class SpannerException extends RuntimeException{
    public SpannerException() {
        super();
    }

    public SpannerException(String message) {
        super(message);
    }

    public SpannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpannerException(Throwable cause) {
        super(cause);
    }

    protected SpannerException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
