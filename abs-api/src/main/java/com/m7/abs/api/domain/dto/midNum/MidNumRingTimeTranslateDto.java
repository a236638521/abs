package com.m7.abs.api.domain.dto.midNum;

import lombok.*;

import java.util.Date;

/**
 * 统一话单推送数据标准
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MidNumRingTimeTranslateDto {
    private String accountId;
    /**
     * 第三方通道绑定记录
     */
    private String channelBindId;
    /**
     * 第三方通道通话记录ID
     */
    private String channelRecordId;

    /**
     * 录音文件地址
     */
    private Date ringTime;
    /**
     * 主叫号码
     */
    private String caller;
    /**
     * 被叫号码
     */
    private String callee;
    /**
     * 中间号
     */
    private String telX;
}
