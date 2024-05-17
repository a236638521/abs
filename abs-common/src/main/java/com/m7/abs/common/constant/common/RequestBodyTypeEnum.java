package com.m7.abs.common.constant.common;

/**
 * 请求数据类型
 */
public enum RequestBodyTypeEnum {

    JSON("JSON", "json类型"),

    FORM("FORM", "表单类型"),

    TEXT("TEXT", "文本类型"),

    XML("XML", "xml文本类型");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    RequestBodyTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public static RequestBodyTypeEnum getInstance(String type) {
        for (RequestBodyTypeEnum typeEnum : RequestBodyTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}
