package com.m7.abs.common.constant.common;

/**
 * 数据通用状态
 */
public enum DataStatusEnum {

    INIT(0, "初始化"),

    ENABLE(1, "启用"),

    DISABLE(2, "禁用");

    private int value;

    private String description;

    DataStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static DataStatusEnum getInstance(int value) {
        DataStatusEnum statusEnum = null;
        for (DataStatusEnum dataStatusEnum : DataStatusEnum.values()) {
            if (dataStatusEnum.getValue() == value) {
                statusEnum = dataStatusEnum;
                break;
            }
        }
        return statusEnum;
    }
}
