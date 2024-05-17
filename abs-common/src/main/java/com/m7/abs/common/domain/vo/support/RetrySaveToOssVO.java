package com.m7.abs.common.domain.vo.support;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author zhuhf
 */
@Data
@Builder
public class RetrySaveToOssVO {
    /**
     * 重新执行提交成功的任务 id
     */
    private List<String> retrySubmitTask;
    /**
     * 正在执行中或者已经执行成功的任务 id
     */
    private List<String> successfulOrExecutingTask;
}
