package com.m7.abs.admin.feignClient.retryer;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class CommonFeignRetry extends Retryer.Default {

    public CommonFeignRetry() {//重试5次最大间隔时间1秒
        this(100, SECONDS.toMillis(5), 5);
    }

    public CommonFeignRetry(long period, long maxPeriod, int maxAttempts) {
        super(period, maxPeriod, maxAttempts);
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        log.error("begin to retry：{} ,{}" , e.getMessage(), e);
        super.continueOrPropagate(e);
    }
}
