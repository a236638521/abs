package com.m7.abs.common.constant.common;

/**
 * 字段类型
 */
public enum FieldTypeEnum {

    STRING("STRING", "字符串"),

    INT("INTEGER", "int类型"),

    DOUBLE("DOUBLE", "其他数字类型"),

    OBJECT("OBJECT", "map类型"),

    BOOLEAN("BOOLEAN", "bool类型"),

    ARRAY("ARRAY", "数组");

    private String type;

    private String description;

    FieldTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static FieldTypeEnum getInstance(String type) {
        for (FieldTypeEnum typeEnum : FieldTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}
