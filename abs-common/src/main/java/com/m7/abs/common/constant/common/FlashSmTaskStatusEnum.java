package com.m7.abs.common.constant.common;

import lombok.Getter;

/**
 * 闪信发送状态
 */
@Getter
public enum FlashSmTaskStatusEnum {
    WAITING_FOR_RECEIPT("1", "等待回执"),
    RECEIVED_SUCCESS("2", "成功接收回执"),
    ;

    private String value;
    private String description;

    FlashSmTaskStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static FlashSmTaskStatusEnum getInstance(String value) {
        FlashSmTaskStatusEnum statusEnum = null;
        for (FlashSmTaskStatusEnum midNumCallStatusEnum : FlashSmTaskStatusEnum.values()) {
            if (midNumCallStatusEnum.getValue().equals(value)) {
                statusEnum = midNumCallStatusEnum;
                break;
            }
        }
        return statusEnum;
    }
}
