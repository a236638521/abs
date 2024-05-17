package com.m7.abs.common.constant.common;

/**
 * 字段随机类型
 */
public enum RandomTypeEnum {

    STRING("STRING", "字符串,默认长度32"),

    INT("INT", "整数，默认0-9"),

    NUMBER("NUMBER", "小数 默认0-9"),

    DATA_RUBS("DATA_RUBS", "时间搓, 默认10位"),

    DATA_STRING("DATA_STRING", "时间字符串  默认格式yyyy-MM-dd HH:mm:ss");

    private String type;

    private String description;

    RandomTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public static RandomTypeEnum getInstance(String type) {
        for (RandomTypeEnum typeEnum : RandomTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}
