package com.m7.abs.api.domain.dto.midNum;

import lombok.*;

/**
 * 统一话单推送数据标准
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MidNumRecordUrlTranslateDto {
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
    private String recordFileUrl;
}
