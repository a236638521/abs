package com.m7.abs.common.constant.common;

import lombok.Getter;

/**
 * 闪信发送状态
 */
@Getter
public enum FlashSmDeliveryStatusEnum {
    DELIVERY_SUCCEED("1", "投递成功,消息已加入推送队列."),
    DELIVERY_FAILED("2", "投递失败"),
    DELIVERY_INTERVAL_LIMIT("101", "不能短时间内连续向同一个号码投递"),
    DELIVERY_DAY_LIMIT("102", "一天内向同一个号码投递次数达到上限"),
    INVALID_TARGET_NUMBER("103", "无法识别的投递号码"),
    TARGET_IN_BLACKLIST("104", "投递号码在黑名单中"),
    TARGET_SEGMENT_IS_RESTRICTED("105", "投递号段受限"),
    BEYOND_FLUID_CONTROL("106", "超出流量限制"),
    DELIVERY_TYPE_NOT_MATCHING_TEMPLATE("107", "投递类型与模版不匹配"),
    REACH_MAXIMUM_DELIVERY_LIMIT("109", "到达套餐上限停发"),
    ILLEGAL_WORD_FOUND("110", "含有敏感词"),
    API_RESOURCE_UNAVAILABLE("111", "资源不足"),
    LENGTH_OF_THE_MESSAGE_CONTENT_EXCEEDS_LIMIT("112", "消息内容长度超过限制"),
    NOT_SUPPORTED_BY_THE_OPERATOR("113", "运营商不支持"),
    SYSTEM_BUSY("114", "系统繁忙"),
    ;

    private String value;
    private String description;

    FlashSmDeliveryStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static FlashSmDeliveryStatusEnum getInstance(String value) {
        FlashSmDeliveryStatusEnum statusEnum = null;
        for (FlashSmDeliveryStatusEnum midNumCallStatusEnum : FlashSmDeliveryStatusEnum.values()) {
            if (midNumCallStatusEnum.getValue().equals(value)) {
                statusEnum = midNumCallStatusEnum;
                break;
            }
        }
        return statusEnum;
    }
}
