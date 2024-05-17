package com.m7.abs.api.domain.dto.flashSm;

import lombok.*;

/**
 * 统一话单推送回执数据标准
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlashSmRecordRespDto {
    /**
     * 闪信推送状态入库Dto
     */
    private FlashSmTranslateDto translateDto;

    /**
     * 接口返回数据
     */
    private Object respData;
}
