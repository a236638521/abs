package com.m7.abs.api.domain.dto.midNum;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MidNumSmsTranslateDto {

    /**
     * 第三方通道绑定记录
     */
    private String channelBindId;
    /**
     * 第三方通道通话记录ID
     */
    private String channelRecordId;
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
    /**
     * 短信发送结果
     */
    private String smsResult;
    /**
     * 短信发送条数（长短信被拆分的条数）
     */
    private int smsNumber;
    /**
     * 发送短信时间 格式:yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date smsTime;

}
