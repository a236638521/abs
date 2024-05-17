package com.m7.abs.support.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * @author zhuhf
 */
public class CommonConstant {
    /**
     * 默认情况下，一分钟重试一次
     */
    public static final long DEFAULT_RETRY_INTERVAL_SECOND = TimeUnit.MINUTES.toSeconds(1L);
}
