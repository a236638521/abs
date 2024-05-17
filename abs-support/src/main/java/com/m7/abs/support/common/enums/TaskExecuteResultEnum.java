package com.m7.abs.support.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author zhuhf
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum TaskExecuteResultEnum {
    /**
     * 成功
     */
    SUCCESS("success"),
    /**
     * 失败
     */
    FAILED("failed");
    private final String code;
}
