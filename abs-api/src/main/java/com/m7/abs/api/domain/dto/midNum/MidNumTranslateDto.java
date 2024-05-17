package com.m7.abs.api.domain.dto.midNum;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m7.abs.common.constant.common.CarrierTypeEnum;
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
public class MidNumTranslateDto {

    /**
     * 第三方通道绑定记录
     */
    private String channelBindId;
    /**
     * 第三方通道通话记录ID
     */
    private String channelRecordId;
    /**
     * 第三方通道groupID
     */
    private String channelGroupId;
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
     * 通话结果
     */
    private String result;
    /**
     *
     */
    private String callDisplay;
    /**
     * 主叫显示号码
     */
    private String callerShow;
    /**
     * 被叫显示号码
     */
    private String calledShow;
    /**
     * 主叫区号
     */
    private String callerArea;
    /**
     * 被叫区号
     */
    private String calledArea;
    /**
     * 主叫运营商
     */
    private String callerCarrier;
    /**
     * 被叫运营商
     */
    private String calledCarrier;
    /**
     * 是否录音
     */
    private boolean callRecording;
    /**
     * 通话时长
     */
    private long billDuration;
    /**
     * 呼叫开始时间 格式:yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 振铃时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alertingTime;
    /**
     * 接听时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date connectTime;
    /**
     * 挂断时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseTime;
    /**
     * 录音文件地址
     */
    private String recordFileUrl;
    /**
     * 挂机放:
     * 0:平台结束;
     * 1:主叫结束;
     * 2:被叫结束;
     * 3:未知;
     */
    private String releaseDir;
}
