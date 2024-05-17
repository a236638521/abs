package com.m7.abs.support.core.exception;

/**
 * @author zhuhf
 */
public class SaveToOssFailedException extends NsqTaskException {
    public SaveToOssFailedException() {
        super();
    }

    public SaveToOssFailedException(String message) {
        super(message);
    }

    public SaveToOssFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveToOssFailedException(Throwable cause) {
        super(cause);
    }

    protected SaveToOssFailedException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
