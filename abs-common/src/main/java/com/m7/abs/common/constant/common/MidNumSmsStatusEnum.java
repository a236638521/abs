package com.m7.abs.common.constant.common;

import lombok.Getter;

/**
 * 短信发送结果：
 * ‘0’: 短信网关返回发送失败；
 * ‘1’: 发送短信成功；
 * ‘2’: 等待短信结果超时；
 * ‘3’: 中间号状态不正确；
 * ‘4’: 企业状态不正确；
 * ‘5’: 企业未开启短信功能；
 * ‘6’: 无绑定关系；
 * ‘7’: 订单超过有效期；（自1.9.18版本开始弃用）
 * ‘8’: 订单状态不正确；
 * ‘9’:	平台系统异常；
 * ‘19’: 测试账号测试调用次数超出限额；
 * ‘20’: 短信内容含涉敏关键词；
 * ‘21’: 短信内容含违规关键词；
 */
@Getter
public enum MidNumSmsStatusEnum {
    FAIL("0", "短信网关返回发送失败"),
    SUCCESS("1", "发送短信成功"),
    TIMEOUT("2", "等待短信结果超时"),
    MID_NUM_STATUS_ERROR("3", "中间号状态不正确"),
    ENTERPRISE_STATUS_ERROR("4", "企业状态不正确"),
    ENTERPRISE_NO_FUNCTION("5", "企业未开启短信功能；"),
    NO_RELATION("6", "无绑定关系"),
    ORDER_TIMEOUT("7", "订单超过有效期"),
    ORDER_STATUS_ERROR("8", "订单状态不正确"),
    SYSTEM_ERROR("9", "平台系统异常"),
    OVERCLOCK("19", "测试账号测试调用次数超出限额"),
    CONTAINS_SENSITIVE_KEYWORDS("20", "短信内容含涉敏关键词"),
    CONTAINS_ILLEGAL_KEYWORDS("21", "短信内容含违规关键词");

    private String value;
    private String description;

    MidNumSmsStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static MidNumSmsStatusEnum getInstance(String value) {
        MidNumSmsStatusEnum statusEnum = null;
        for (MidNumSmsStatusEnum midNumCallStatusEnum : MidNumSmsStatusEnum.values()) {
            if (midNumCallStatusEnum.getValue().equals(value)) {
                statusEnum = midNumCallStatusEnum;
                break;
            }
        }
        return statusEnum;
    }
}
