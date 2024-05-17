package com.m7.abs.common.domain.vo.reportexport;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * 小号话单导出VO
 *
 * @author Kejie Peng
 * @date 2023年 03月22日 15:08:04
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidNumCdrExportRequestVO {
    /**
     * 导出任务ID
     */
    @NotEmpty
    private String taskId;
    @NotEmpty
    private String accountId;
    /**
     * 导出开始日期
     */
    @NotEmpty
    private String startTime;
    /**
     * 导出结束日期
     */
    @NotEmpty
    private String endTime;

    /**
     * 导出类型: excel;csv
     */
    private String type;

    /**
     * bill账户ID
     */
    private String billAccountId;
}
