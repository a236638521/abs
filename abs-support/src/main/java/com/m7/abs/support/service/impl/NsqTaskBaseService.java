package com.m7.abs.support.service.impl;

import com.google.common.base.MoreObjects;
import com.m7.abs.common.properties.nsq.NsqProperties;
import com.m7.abs.common.utils.SnowflakeIdWorker;
import com.m7.abs.support.common.constant.CommonConstant;
import com.m7.abs.support.core.nsq.NsqTaskExecutor;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuhf
 */
public abstract class NsqTaskBaseService {
    /**
     * 默认重试时间间隔误差，单位秒
     */
    protected static final long DEFAULT_RETRY_INTERVAL_TIME_DEVIATION =
            TimeUnit.SECONDS.toMillis(CommonConstant.DEFAULT_RETRY_INTERVAL_SECOND / 2);

    protected final NsqTaskExecutor nsqTaskExecutor;
    protected final JsonComponent jsonComponent;
    protected final AbsSupportProperties absSupportProperties;
    protected final NsqProperties nsqProperties;
    protected final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    private final long retryIntervalTimeDeviation;

    protected NsqTaskBaseService(
            NsqTaskExecutor nsqTaskExecutor,
            JsonComponent jsonComponent,
            AbsSupportProperties absSupportProperties,
            NsqProperties nsqProperties,
            Integer retryIntervalTimeDeviation) {
        this.nsqTaskExecutor = nsqTaskExecutor;
        this.jsonComponent = jsonComponent;
        this.absSupportProperties = absSupportProperties;
        this.nsqProperties = nsqProperties;
        this.retryIntervalTimeDeviation =
                MoreObjects.firstNonNull(retryIntervalTimeDeviation, (int) DEFAULT_RETRY_INTERVAL_TIME_DEVIATION);
    }

    /**
     * 单位，秒/s
     * @param retryInterval 重试间隔
     * @return 重试间隔
     */
    protected Long getRetryInterval(Long retryInterval) {
        if (retryInterval == null) {
            retryInterval = CommonConstant.DEFAULT_RETRY_INTERVAL_SECOND;
        }
        if (retryInterval.intValue() != retryInterval) {
            retryInterval = (long) Integer.MAX_VALUE;
        }
        // 误差
        retryInterval -= retryIntervalTimeDeviation;
        retryInterval += ThreadLocalRandom.current().nextInt((int) retryIntervalTimeDeviation);
        return retryInterval;
    }
}
