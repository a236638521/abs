package com.m7.abs.support.core.exception;

/**
 * @author zhuhf
 */
public class NsqTaskException extends RuntimeException {
    public NsqTaskException() {
        super();
    }

    public NsqTaskException(String message) {
        super(message);
    }

    public NsqTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public NsqTaskException(Throwable cause) {
        super(cause);
    }

    protected NsqTaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
