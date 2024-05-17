package com.m7.abs.api.delayed.bean;

import lombok.*;

/**
 * @author Kejie Peng
 * @date 2023年 04月14日 10:15:47
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class RetryBean {
    private String traceId;
    /**
     * 当前重试次数
     */
    private int currentCount;
    /**
     * 最大重试次数
     */
    private int maxTime;
    /**
     * 消息体
     */
    private UpdateMidNumCdrOssIdBean data;

    public RetryBean() {
        this.currentCount = 1;
        this.maxTime = 5;
    }
}
