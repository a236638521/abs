package com.m7.abs.api.domain.dto.flashSm;

import lombok.*;

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
public class FlashSmDispatcherDto {
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 账户编号
     */
    private String accountId;
    /**
     * 任务列表
     */
    private List<FlashSmDeliveryDispatcherResultDto> deliveryResult;
}
