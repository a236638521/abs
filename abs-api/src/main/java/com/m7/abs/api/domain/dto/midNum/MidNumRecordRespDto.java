package com.m7.abs.api.domain.dto.midNum;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MidNumRecordRespDto {
    /**
     * 话单入库Dto
     */
    private MidNumTranslateDto translateDto;

    /**
     * 录音入库Dto
     */
    private MidNumRecordUrlTranslateDto recordUrlDto;

    /**
     * 通话振铃时间对应事件
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private MidNumRingTimeTranslateDto ringTimeDto;
    /**
     * 接口返回数据
     */
    private Object respData;
}
