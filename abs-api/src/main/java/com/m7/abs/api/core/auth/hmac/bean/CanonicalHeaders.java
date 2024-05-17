package com.m7.abs.api.core.auth.hmac.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/7/8 13:43
 */
@Getter
@Setter
public class CanonicalHeaders {

    /**
     * 请求时间戳 TC时间，格式为 yyyy-MM-dd'T'HH:mm:ssZ
     * Unix timestamp 单位/秒(s)
     */
    @NotEmpty
    private String timestamp;
    /**
     * UUID随机字符串
     */
    @NotEmpty
    private String nonce;
}
