package com.m7.abs.common.domain.dto;

import lombok.*;

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
public class FlashSmDeliveryReportResultDto {
    /**
     * 接收对象
     */
    private String target;
    /**
     * 推送状态
     */
    private String status;
    /**
     * 消息内容
     */
    private String msg;
}
