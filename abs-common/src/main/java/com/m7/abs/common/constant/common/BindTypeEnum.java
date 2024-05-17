package com.m7.abs.common.constant.common;

public enum BindTypeEnum {

    AXB("AXB", "AXB绑定"),

    AX("AX", "AX绑定"),

    GXB("GXB", "GXB绑定"),

    AXYB("AXYB", "AXYB绑定");
    private String type;

    private String description;

    BindTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}