package com.m7.abs.common.constant.common;

/**
 * @author kejie peng
 */
public enum AccountPushUrlTypeEnum {
    AXB_CDR_PUSH_URL("AXB_CDR", "AXB话单推送地址"),
    AX_CDR_PUSH_URL("AX_CDR", "AX话单推送地址"),
    X_SMS_CDR_PUSH_URL("X_SMS_CDR", "小号短信推送地址"),
    FLASH_SMS_CDR_PUSH_URL("FLASH_SMS_CDR", "闪信话单推送地址"),
    ;
    private String type;

    private String description;

    AccountPushUrlTypeEnum(String type, String description) {
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
