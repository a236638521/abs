package com.m7.abs.api.projectHandler.midNum.model.tyzn;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TYZNRecordReq {

    /**
     * 账户id
     */
    private String appId;

    /**
     * 请求唯一id
     */
    private String requestId;

    /**
     * A号码
     */
    private String telA;

    /**
     * 虚拟号码
     */
    private String telX;

    /**
     * B号码
     */
    private String telB;

    /**
     * 绑定唯一id，outtransfer绑定时推送值为0
     */
    private String subId;

    /**
     * 呼叫类型： 10：通话主叫 11：通话被叫 12：短信发送 13：短信接收 20：呼叫不允许 21：回呼类型 30：短信不允许
     */
    private String callType;

    /**
     * 发起呼叫时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String callTime;

    /**
     * 开始振铃时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String ringingTime;

    /**
     * 开始通话时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String startTime;

    /**
     * 通话结束时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String releaseTime;

    /**
     * 释放方向，1 表示主叫 2 表示被叫 0 表示平台释放
     */
    private String releaseDir;

    /**
     * 释放原因，
     */
    private String releaseCause;

    /**
     * 录音地址
     */
    private String recordUrl;

    /**
     * 通话总时长
     */
    private long totalDuration;
    /**
     * 账单计费市场
     */
    private long billDuration;

    /**
     * 彩铃音地址
     */
    private String ringingRecordUrl;

    /**
     * 录音模式，1:主叫在左声道 2:主叫在右声道 3:混音
     */
    private String recordMode;

    /**
     * 呼叫唯一id
     */
    private String callId;

    /**
     * 录音控制，0：本次呼叫不录音 1：本次呼叫录音
     */
    private String callRecording;

    /**
     * 运营商自定义字段
     */
    private String remark;

    /**
     * 呼转号码
     */
    private String telredir;
}
