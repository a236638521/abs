package com.m7.abs.common.domain.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 统一话单推送数据标准
 * @author Kejie Peng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlashSmReportDto {
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 账户编号
     */
    private String accountId;
    /**
     * 任务创建时间
     */
    private Date createTime;
    /**
     * 任务列表
     */
    private List<FlashSmDeliveryReportResultDto> deliveryResult;
}
