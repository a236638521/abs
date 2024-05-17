package com.m7.abs.common.constant.common;

import lombok.Getter;

@Getter
public enum CarrierTypeEnum {
    CHINA_UNICOM("china_unicom", "中国联通"),

    CHINA_MOBILE("china_mobile", "中国移动"),

    CHINA_TELECOM("china_telecom", "中国电信"),

    UNKNOWN("unknown", "未知");


    private String type;

    private String description;

    CarrierTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public static CarrierTypeEnum getInstance(String value) {
        CarrierTypeEnum resultEnum = null;
        for (CarrierTypeEnum typeEnum : CarrierTypeEnum.values()) {
            if (typeEnum.getType().equals(value)) {
                resultEnum = typeEnum;
                break;
            }
        }
        return resultEnum;
    }
}
