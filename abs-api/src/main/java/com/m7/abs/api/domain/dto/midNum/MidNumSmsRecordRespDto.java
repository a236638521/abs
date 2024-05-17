package com.m7.abs.api.domain.dto.midNum;

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
public class MidNumSmsRecordRespDto {
    /**
     * 短信话单入库Dto
     */
    private MidNumSmsTranslateDto translateDto;
    /**
     * 接口返回数据
     */
    private Object respData;
}
