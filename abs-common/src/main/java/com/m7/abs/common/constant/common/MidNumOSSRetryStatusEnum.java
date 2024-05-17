package com.m7.abs.common.constant.common;

import lombok.Getter;

/**
 * 小号呼叫结束状态
 * 001：  记录已加入重试队列
 * 002：  未查到该记录
 * 003：  超出规定时间,无法重新下载.
 * 004：  未找到录音推送记录
 * 005：  该记录占时无法重试
 */
@Getter
public enum MidNumOSSRetryStatusEnum {
    RETRY_SUCCESS("001", "记录已加入重试队列"),
    NO_RECORD("002", "未查到该记录"),
    EXPIRED("003", "超出规定时间,无法重新下载."),
    NO_OSS_RECORD("004", "未找到录音推送记录"),
    NOT_SUPPORT("005", "该记录暂时无法重试");

    private String value;
    private String description;

    MidNumOSSRetryStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static MidNumOSSRetryStatusEnum getInstance(String value) {
        MidNumOSSRetryStatusEnum statusEnum = null;
        for (MidNumOSSRetryStatusEnum midNumCallStatusEnum : MidNumOSSRetryStatusEnum.values()) {
            if (midNumCallStatusEnum.getValue().equals(value)) {
                statusEnum = midNumCallStatusEnum;
                break;
            }
        }
        return statusEnum;
    }
}
