package com.m7.abs.common.constant.common;

/**
 * 响应数据类型
 */
public enum ResponseTypeEnum {

    JSON("JSON", "JSON类型"),

    XML("XML", "XML类型"),

    TEXT("TEXT", "文本类型");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    ResponseTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public static ResponseTypeEnum getInstance(String type) {
        for (ResponseTypeEnum typeEnum : ResponseTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}
