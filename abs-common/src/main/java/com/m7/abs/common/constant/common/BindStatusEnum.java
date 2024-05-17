package com.m7.abs.common.constant.common;

public enum BindStatusEnum {

    BINDING("BINDING", "绑定中"),

    UNBIND("UNBIND", "已解绑"),

    EXPIRED("EXPIRED", "已过期");

    private String type;

    private String description;

    BindStatusEnum(String type, String description) {
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