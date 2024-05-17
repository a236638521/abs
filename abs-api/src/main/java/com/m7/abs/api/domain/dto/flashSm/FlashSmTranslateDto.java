package com.m7.abs.api.domain.dto.flashSm;

import lombok.*;

import java.util.List;

/**
 * 统一话单推送数据标准
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlashSmTranslateDto {
    private String taskId;
    private List<FlashSmDeliveryResultDto> deliveryResult;
}
