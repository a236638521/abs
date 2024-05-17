package com.m7.abs.common.constant.common;

import lombok.Getter;

/**
 * 闪信回执状态
 * 1：  投递成功
 * 2:	主叫挂机
 * 3:	回执报告超时
 * 500:	未知处理状态
 * 501:	服务处理错误
 */
@Getter
public enum FlashSmDeliveryResultEnum {
    DELIVERY_SUCCEED("1", "投递成功"),
    DELIVERY_FAILED("2", "投递失败"),
    REPORT_TIMEOUT("3", "回执报告超时"),
    UNKNOWN_ERROR("500", "未知处理状态"),
    SERVICE_ERROR("501", "服务处理错误");

    private String value;
    private String description;

    FlashSmDeliveryResultEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static FlashSmDeliveryResultEnum getInstance(String value) {
        FlashSmDeliveryResultEnum statusEnum = null;
        for (FlashSmDeliveryResultEnum midNumCallStatusEnum : FlashSmDeliveryResultEnum.values()) {
            if (midNumCallStatusEnum.getValue().equals(value)) {
                statusEnum = midNumCallStatusEnum;
                break;
            }
        }
        return statusEnum;
    }
}
