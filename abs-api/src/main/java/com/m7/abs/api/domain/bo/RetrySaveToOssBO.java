package com.m7.abs.api.domain.bo;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author zhuhf
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RetrySaveToOssBO {
    @NotBlank
    private String taskId;
    /**
     * 推送失败时的重试间隔时间，单位为秒(s)
     */
    private Long retryInterval;
}
