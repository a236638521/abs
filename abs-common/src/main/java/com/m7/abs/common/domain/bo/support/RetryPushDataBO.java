package com.m7.abs.common.domain.bo.support;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhuhf
 */
@Data
public class RetryPushDataBO {
    @NotBlank
    private String taskId;
    /**
     * 什么时候推送数据的时间戳，毫秒
     */
    private Long sendTime;
    /**
     * 推送失败时的重试间隔时间，单位为秒(s)
     */
    private Long retryInterval;
}
