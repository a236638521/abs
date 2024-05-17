package com.m7.abs.common.constant.common;

import lombok.Getter;

/**
 * 小号呼叫结束状态
 * 0：  正常挂机
 * 1:	主叫挂机
 * 2:	被叫挂机
 * 3:	主叫放弃
 * 4:	被叫无应答
 * 5:	被叫忙
 * 6:	被叫不可及
 * 7:	路由失败
 * 8:	中间号状态异常
 * 10:	平台系统异常
 * 11: 关机
 * 12: 停机
 * 13: 拒接
 * 14: 空号
 */
@Getter
public enum MidNumCallStatusEnum {
    NORMAL_HANGUP("0", "正常挂断"),
    CALLER_ABANDONMENT("3", "主叫放弃"),
    CALLED_NO_ANSWER("4", "被叫无应答"),
    CALLED_BUSY("5", "被叫忙"),
    CALLED_OUT_OF_REACH("6", "被叫不可及"),
    ROUTING_FAILED("7", "路由失败"),
    TEL_X_ERROR_STATE("8", "中间号状态异常"),
    PLATFORM_ERROR("10", "平台系统异常"),
    POWER_OFF("11", "关机"),
    OUT_OF_SERVICE("12", "停机"),
    REFUSE("13", "拒接"),
    EMPTY_NUMBER("14", "空号"),
    CALL_FAIL("15", "呼叫失败");

    private String value;
    private String description;

    MidNumCallStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static MidNumCallStatusEnum getInstance(String value) {
        MidNumCallStatusEnum statusEnum = null;
        for (MidNumCallStatusEnum midNumCallStatusEnum : MidNumCallStatusEnum.values()) {
            if (midNumCallStatusEnum.getValue().equals(value)) {
                statusEnum = midNumCallStatusEnum;
                break;
            }
        }
        return statusEnum;
    }
}
