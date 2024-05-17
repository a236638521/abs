package com.m7.abs.common.domain.bo.support;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhuhf
 */
@Data
public class RetrySaveToOssBO {
    @NotBlank
    private String taskId;
    /**
     * 推送失败时的重试间隔时间，单位为秒(s)
     */
    private Long retryInterval;
}
